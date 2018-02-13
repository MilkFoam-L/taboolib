package me.skymc.taboolib.string.language2.type;

import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import lombok.Getter;
import me.skymc.taboolib.TabooLib;
import me.skymc.taboolib.display.TitleUtils;
import me.skymc.taboolib.message.MsgUtils;
import me.skymc.taboolib.string.language2.Language2Value;

/**
 * @author sky
 * @since 2018��2��13�� ����3:58:07
 */
public class Language2Title {
	
	private static final String KEY_TITLE = "    title: ";
	private static final String KEY_SUBTITLE = "    subtitle: ";
	private static final String KEY_STAYRULE = "    stay: ";
	
	@Getter
	private String title = "";
	
	@Getter
	private String subtitle = "";
	
	@Getter
	private int fade1 = 0;
	
	@Getter
	private int fade2 = 0;
	
	@Getter
	private int stay = 20;
	
	@Getter
	private Language2Value value;
	
	public Language2Title(Language2Value value) {
		// ������ʼ��
		this.value = value;
		
		// �����ı�
		for (String message : value.getLanguageValue()) {
			try {
				// �����
				if (message.startsWith(KEY_TITLE)) {
					title = message.substring(KEY_TITLE.length());
				}
				// С����
				else if (message.startsWith(KEY_SUBTITLE)) {
					subtitle = message.substring(KEY_SUBTITLE.length());
				}
				// ����ʱ��
				else if (message.startsWith(KEY_STAYRULE)) {
					String rule = message.substring(KEY_STAYRULE.length());
					fade1 = Integer.valueOf(rule.split("\\|")[0]);
					stay = Integer.valueOf(rule.split("\\|")[1]);
					fade2 = Integer.valueOf(rule.split("\\|")[2]);
				}
			}
			catch (Exception e) {
				// ʶ���쳣
				title = ChatColor.DARK_RED + "[<ERROR-10: " + value.getLanguageKey() + ">]";
				subtitle = ChatColor.DARK_RED + "[<ERROR-10: " + value.getLanguageKey() + ">]";
			}
		}
	}
	
	/**
	 * ���͸����
	 * 
	 * @param player ���
	 */
	public void send(Player player) {
		// ���汾
		if (TabooLib.getVerint() < 10800) {
			player.sendMessage(ChatColor.DARK_RED + "[<ERROR-31: " + value.getLanguageKey() + ">]");
		}
		// ������
		else if (player != null) {
			TitleUtils.sendTitle(player, value.setPlaceholder(title, player), value.setPlaceholder(subtitle, player), fade1, stay, fade2);
		}
		else {
			Bukkit.getConsoleSender().sendMessage(value.setPlaceholder(title, player) + ", " + value.setPlaceholder(subtitle, player));
		}
	}
}