package com.bluepowermod.client.render.placement_preview;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.bluepowermod.init.BPClientConfig;
import net.minecraft.client.resources.model.BakedModel;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;

/**
 * Wrapper class around the vanilla block renderer so we don't have to copy five
 * functions verbatim
 **/
public class BlockPreviewRenderer extends ModelBlockRenderer
{
	private static BlockPreviewRenderer INSTANCE;
	
	public static BlockPreviewRenderer getInstance(ModelBlockRenderer baseRenderer)
	{
		if (INSTANCE == null || INSTANCE.blockColors != baseRenderer.blockColors)
		{
			INSTANCE = new BlockPreviewRenderer(baseRenderer);
		}
		
		return INSTANCE;
	}
	public BlockPreviewRenderer(ModelBlockRenderer baseRenderer)
	{
		super(baseRenderer.blockColors);
	}

	// invoked from the DrawHighlightEvent.HighlightBlock event
	public static void renderBlockPreview(BlockPos pos, BlockState state, Level level, Vec3 currentRenderPos, PoseStack matrix, MultiBufferSource renderTypeBuffer) {
		BakedModel model = PlacementPreviewReloadListener.INSTANCE.getModelFromState(state);
		if (model == null) return;
		matrix.pushPose();
	
		// the current position of the matrix stack is the position of the player's
		// viewport (the head, essentially)
		// we want to move it to the correct position to render the block at
		double offsetX = pos.getX() - currentRenderPos.x();
		double offsetY = pos.getY() - currentRenderPos.y();
		double offsetZ = pos.getZ() - currentRenderPos.z();
		matrix.translate(offsetX, offsetY, offsetZ);
	
		BlockRenderDispatcher blockDispatcher = Minecraft.getInstance().getBlockRenderer();
		ModelBlockRenderer renderer = getInstance(blockDispatcher.getModelRenderer());
		renderer.tesselateBlock(
			level,
			model,
			state,
			pos,
			matrix,
			renderTypeBuffer.getBuffer(Sheets.translucentCullBlockSheet()),
			false,
			level.random,
			state.getSeed(pos),
			OverlayTexture.NO_OVERLAY,
			ModelData.EMPTY,
			RenderType.translucent());
	
		matrix.popPose();
	}

	@Override
	public void putQuadData(BlockAndTintGetter level, BlockState state, BlockPos pos, VertexConsumer buffer, PoseStack.Pose matrixEntry, BakedQuad quadIn,
		float tintA, float tintB, float tintC, float tintD, int brightness0, int brightness1, int brightness2, int brightness3, int combinedOverlayIn) {
		float r=1F;
		float g=1F;
		float b=1F;
		if (quadIn.isTinted()) {
			int i = this.blockColors.getColor(state, level, pos, quadIn.getTintIndex());
			r = (i >> 16 & 255) / 255.0F;
			g = (i >> 8 & 255) / 255.0F;
			b = (i & 255) / 255.0F;
		}
		if (quadIn.isShade()) { // better name: shouldApplyDiffuseLighting
			float shade = level.getShade(quadIn.getDirection(), true);
			r *= shade;
			g *= shade;
			b *= shade;
		}

		// use our method below instead of adding the quad in the usual manner
		addTransparentQuad(matrixEntry, quadIn, new float[] { tintA, tintB, tintC, tintD }, r, g, b, new int[] { brightness0, brightness1, brightness2, brightness3 },
			combinedOverlayIn, true, buffer);
	}
	
	// as IVertexBuilder::addQuad except when we add the vertex, we add an opacity float instead of 1.0F
	public static void addTransparentQuad(PoseStack.Pose matrixEntry, BakedQuad quad, float[] colorMuls, float r, float g, float b, int[] vertexLights,
		int combinedOverlayIn, boolean mulColor, VertexConsumer buffer) {
		int[] vertexData = quad.getVertices();
		Vec3i faceVector3i = quad.getDirection().getNormal();
		Vector3f faceVector = new Vector3f(faceVector3i.getX(), faceVector3i.getY(), faceVector3i.getZ());
		Matrix4f matrix = matrixEntry.pose();
		faceVector.mul(matrixEntry.normal());
		
		int vertexDataEntries = vertexData.length / 8;

		try (MemoryStack memorystack = MemoryStack.stackPush()) {
			ByteBuffer bytebuffer = memorystack.malloc(DefaultVertexFormat.BLOCK.getVertexSize());
			IntBuffer intbuffer = bytebuffer.asIntBuffer();

			for (int vertexIndex = 0; vertexIndex < vertexDataEntries; ++vertexIndex) {
				((Buffer) intbuffer).clear();
				intbuffer.put(vertexData, vertexIndex * 8, 8);
				float x = bytebuffer.getFloat(0);
				float y = bytebuffer.getFloat(4);
				float z = bytebuffer.getFloat(8);
				float red = colorMuls[vertexIndex] * r;
				float green = colorMuls[vertexIndex] * g;
				float blue = colorMuls[vertexIndex] * b;
				
				
				if (mulColor) {
					float redMultiplier = (bytebuffer.get(12) & 255) / 255.0F;
					float greenMultiplier = (bytebuffer.get(13) & 255) / 255.0F;
					float blueMultiplier = (bytebuffer.get(14) & 255) / 255.0F;
					red = redMultiplier * red;
					green = greenMultiplier * green;
					blue = blueMultiplier * blue;
				}
				
				// this is the important part
				float alpha = BPClientConfig.CONFIG.previewPlacementOpacity.get().floatValue();

				int light = buffer.applyBakedLighting(vertexLights[vertexIndex], bytebuffer);
				float texU = bytebuffer.getFloat(16);
				float texV = bytebuffer.getFloat(20);
				Vector4f posVector = new Vector4f(x, y, z, 1.0F);
				posVector.mul(matrix);
				buffer.applyBakedNormals(faceVector, bytebuffer, matrixEntry.normal());
				buffer.vertex(posVector.x(), posVector.y(), posVector.z(), red, green, blue, alpha, texU, texV,
					combinedOverlayIn, light, faceVector.x(), faceVector.y(), faceVector.z());
			}
		}
	}
}
