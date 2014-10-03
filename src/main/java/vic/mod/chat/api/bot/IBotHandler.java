package vic.mod.chat.api.bot;

import java.io.File;

public interface IBotHandler 
{
	public IChannelBase getChannelForName(String name);
	
	/** The global channel **/
	public IChannelBase getDefaultChannel();
	
	public IChatEntity getChatEntityForName(String name);
	
	public IChatEntity getServer();
	
	public boolean isPlayerOnline(String name);
	
	public boolean isOnChannel(IChatEntity entity, IChannelBase channel);
	
	/** Should be used with caution, will have the same priority as the server sending a message. Appears on every channel **/
	public void sendGlobalMessage(String message);
	
	public void sendMessage(IChannelBase channel, String message);
	
	public void sendPrivateMessage(IChatEntity entity, String message);
	
	public void sendCommand(String command, String[] args);
	
	public File getBotDir();
}
