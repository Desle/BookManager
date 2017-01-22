package com.desle.bookmanager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketDataSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutCustomPayload;

public class BookManager {
	
	private static Map<Player, BookManager> list = new HashMap<Player, BookManager>();
	
	public static BookManager get(Player player) {
		if (BookManager.list.get(player) == null)
			return new BookManager(player);
		
		return BookManager.list.get(player);
	}
	
	private Player player;
	private ItemStack book;
	
	public BookManager(Player player) {
		this.player = player;
		
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		
		this.book = book;
		
		list.put(player, this);
	}
	
	public ItemStack getBook() {
		return this.book;
	}
	
	public void openBook() {       
		int slot = this.player.getInventory().getHeldItemSlot();
		ItemStack item = this.player.getInventory().getItem(slot);
		
		this.player.getInventory().setItem(slot, getBook());
	
	   ByteBuf buf = Unpooled.buffer(256);
	   buf.setByte(0, (byte)0);
	   buf.writerIndex(1);
	
	    PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
	    ((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
	    
	    this.player.getInventory().setItem(slot, item);
	}
	
	public void setPages(List<String> pages) {
		BookMeta bookmeta = (BookMeta) this.book.getItemMeta();
		bookmeta.setPages(pages);
		
		this.book.setItemMeta(bookmeta);
	}

	@SuppressWarnings("unchecked")
	public void setJsonPages(List<IChatBaseComponent> pages) {
		BookMeta bookmeta = (BookMeta) this.book.getItemMeta();
		
		try {
	     Field pagesField = Class.forName("org.bukkit.craftbukkit.v1_11_R1.inventory.CraftMetaBook").getDeclaredField("pages");
	     pagesField.setAccessible(true);
	     
	     List<IChatBaseComponent> pagelist = (List<IChatBaseComponent>) pagesField.get(bookmeta);
	     
	     pagelist.clear();
	     pagelist.addAll(pages);
	     
		} catch (Exception e) {
			player.sendMessage(e.getMessage());
		}
		
		this.book.setItemMeta(bookmeta);
	}
	
	public void setBook(ItemStack book) {
		this.book = book;
	}
	
	public void destroy() {		
		list.remove(this.player);
	}
}
