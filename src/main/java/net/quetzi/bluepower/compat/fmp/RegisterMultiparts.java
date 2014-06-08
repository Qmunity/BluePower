/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.compat.fmp;

import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.part.IBPFacePart;
import net.quetzi.bluepower.api.part.PartRegistry;
import net.quetzi.bluepower.references.Refs;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;

public class RegisterMultiparts implements IPartFactory {
    
    private RegisterMultiparts() {
    
    }
    
    public static void register() {
    
        String[] parts = PartRegistry.getRegisteredParts().toArray(new String[0]);
        
        for (int i = 0; i < parts.length; i++)
            parts[i] = Refs.MODID + "_" + parts[i];
        
        MultiPartRegistry.registerParts(new RegisterMultiparts(), parts);
    }
    
    @Override
    public TMultiPart createPart(String id, boolean client) {
    
        return createPart_(id, client, true);
    }
    
    public static TMultiPart createPart_(String id, boolean client, boolean multipartFactory) {
    
        BPPart part = PartRegistry.createPart(id, multipartFactory);
        if (part != null) {
            if (part instanceof IBPFacePart) {
                return new MultipartFaceBPPart((IBPFacePart) part);
            } else {
                return new MultipartBPPart(part);
            }
        }
        
        return null;
    }
    
}
