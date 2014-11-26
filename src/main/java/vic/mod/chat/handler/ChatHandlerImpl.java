package vic.mod.chat.handler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import vic.mod.chat.VChat;

public abstract class ChatHandlerImpl implements IChatHandler
{
	public ChatHandlerImpl()
	{
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		VChat.handlers.add(this);
	}
	
	@Override public void onServerLoad(FMLServerStartingEvent event) {}
	@Override public void onServerUnload(FMLServerStoppingEvent event) {}
}
