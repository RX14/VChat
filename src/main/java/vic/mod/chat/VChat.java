package vic.mod.chat;

import java.io.File;
import java.util.List;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

import org.apache.logging.log4j.Logger;

import vic.mod.chat.handler.AFKHandler;
import vic.mod.chat.handler.AutoAFKHandler;
import vic.mod.chat.handler.ChannelHandler;
import vic.mod.chat.handler.CommonHandler;
import vic.mod.chat.handler.IChatHandler;
import vic.mod.chat.handler.NickHandler;
import vic.mod.chat.handler.TrackHandler;

import com.google.common.collect.Lists;

@Mod(modid = "vchat", name = "vChat", version = Constants.VERSION, acceptableRemoteVersions = "*")
public class VChat
{
	
	@Instance("vchat")
	public static VChat instance;  
	
	public static BotLoader botLoader;
	
	public static CommonHandler commonHandler;
	public static ChannelHandler channelHandler;
	public static AFKHandler afkHandler;
	public static AutoAFKHandler autoAfkHandler;
	public static NickHandler nickHandler;
	public static TrackHandler trackHandler;
	
	public static List<IChatHandler> handlers = Lists.newArrayList();
	public static Logger logger;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		logger = event.getModLog();
		
		Config.initialize(event.getSuggestedConfigurationFile());
		
		File rootDir = event.getModConfigurationDirectory().getParentFile();
		rootDir.setWritable(true);
		commonHandler = new CommonHandler();
		channelHandler = new ChannelHandler();
		botLoader = new BotLoader();
		
		if(Config.nickEnabled) nickHandler = new NickHandler();
		if(Config.trackEnabled) trackHandler = new TrackHandler();
		if(Config.afkEnabled) 
		{
			afkHandler = new AFKHandler();
			if(Config.autoAfkEnabled) autoAfkHandler = new AutoAFKHandler();
		}
	}
	
	@EventHandler
	public void onServerLoad(FMLServerStartingEvent event)
	{
		for(IChatHandler handler : handlers)
		{
			handler.onServerLoad(event);
		}
	}
	
	@EventHandler
	public void onServerUnload(FMLServerStoppingEvent event)
	{
		for(IChatHandler handler : handlers)
		{
			handler.onServerUnload(event);
		}
	}
}
