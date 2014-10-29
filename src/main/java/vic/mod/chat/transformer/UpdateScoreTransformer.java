package vic.mod.chat.transformer;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class UpdateScoreTransformer implements ITransformHandler {

	@Override
	public byte[] transform(String className, byte[] buffer) 
	{

		InsnList toInject = new InsnList();
		System.out.println("[vchat]: " + "Trying to patch UpdateScore...");

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(buffer);
		classReader.accept(classNode, 0);
		
		List<MethodNode> methods = classNode.methods;
		Iterator<MethodNode> iterator = methods.iterator();
		while (iterator.hasNext()) 
		{
			MethodNode m = iterator.next();
			
			if (m.name.equals("<init>") && (m.desc.equals("(Lnet/minecraft/scoreboard/Score;I)V") || m.desc.equals("(Lazz;I)V"))) 
			{
				for (int i = 0; i < m.instructions.size(); i++) 
				{
					AbstractInsnNode insn = m.instructions.get(i);
					if(insn instanceof FieldInsnNode)
					{
						
						FieldInsnNode fieldInsn = (FieldInsnNode)insn;
						boolean isOb = fieldInsn.name.equals("a");
						if((fieldInsn.name.equals("field_149329_a") || isOb) && fieldInsn.getPrevious().getOpcode() == Opcodes.INVOKEVIRTUAL)
						{
							//FIX The end of standard declaration
							toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
							// Load a reference onto the stack from local variable 1
							toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
							
							if(isOb)
								toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "azz", "e", "()Ljava/lang/String;"));
							else
								toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/scoreboard/Score", "getPlayerName", "()Ljava/lang/String;"));
							
							// Invoke a static method, where the method is identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
							toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "vic/mod/chat/handler/NickHandler", "getPlayerFromSenderName", "(Ljava/lang/String;)Ljava/lang/String;"));
							// Set field to value in an object objectref, where the field is identified by a field reference index in constant pool (indexbyte1 << 8 + indexbyte2)
							toInject.add(new FieldInsnNode(Opcodes.PUTFIELD, className.replace(".", "/"), fieldInsn.name, fieldInsn.desc));
							// Load a reference onto the stack from local variable 0
							toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
							
							//Insert into instructions list
							m.instructions.insertBefore(m.instructions.get(i + 1), toInject);
							
							
							//Write the class and return byte array.
							ClassWriter writer = new ClassWriter(0);
							classNode.accept(writer);
							
							System.out.println("[Vchat]: " + "UpdateScore patched!");
							return writer.toByteArray();
						}
					}
				}
			
			}
		}
		return buffer;
	}

}
