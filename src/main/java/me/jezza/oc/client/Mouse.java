package me.jezza.oc.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.interfaces.MouseAdapter;
import me.jezza.oc.common.interfaces.Request;
import me.jezza.oc.common.utils.ASM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Jezza
 */
@SideOnly(Side.CLIENT)
public class Mouse {
	private static final LinkedBlockingDeque<MouseRequest> REQUESTS = new LinkedBlockingDeque<>();
	protected static final Mouse INSTANCE = new Mouse();
	protected static final Minecraft MC = Minecraft.getMinecraft();

	private MouseRequest active;

	private Mouse() {
	}

	private void onActive() {
		OmnisCore.logger.info(active.modId + " activated Mouse control.");
		Client.blurMouse();
		Client.blurGame();
	}

	private void release() {
		OmnisCore.logger.info(active.modId + " released Mouse control.");
		Client.focusMouse();
		Client.focusGame();
		active = null;
	}

	protected void tick() {
		if (active != null) {
			if (!active.cancelled()) {
				if (active.retrieved()) {
					MouseAdapter adapter = active.adapter;
					final ScaledResolution scaledresolution = new ScaledResolution(MC, MC.displayWidth, MC.displayHeight);
					int scaledWidth = scaledresolution.getScaledWidth();
					int scaledHeight = scaledresolution.getScaledHeight();
					while (org.lwjgl.input.Mouse.next()) {
						int mouseX = org.lwjgl.input.Mouse.getX() * scaledWidth / MC.displayWidth;
						int mouseY = scaledHeight - org.lwjgl.input.Mouse.getY() * scaledHeight / MC.displayHeight - 1;
						int eventButton = org.lwjgl.input.Mouse.getEventButton();
						if (eventButton >= 0) {
							if (org.lwjgl.input.Mouse.getEventButtonState()) {
								adapter.onClick(mouseX, mouseY, eventButton);
							} else {
								adapter.onRelease(mouseX, mouseY, eventButton);
							}
						}
						adapter.mouseChange(mouseX, mouseY);
					}
				}
			} else {
				active.release();
			}
			return;
		}
		if (REQUESTS.isEmpty())
			return;
		while ((active == null || active.cancelled()) && !REQUESTS.isEmpty())
			active = REQUESTS.pollFirst();
		if (active == null)
			return;
		if (active.cancelled()) {
			active = null;
			return;
		}
		OmnisCore.logger.info(active.modId + " acquired Mouse control.");
		active.acquired(true);
	}

	public static Request request(MouseAdapter adapter) {
		MouseRequest request = new MouseRequest(adapter);
		OmnisCore.logger.info(request.modId + " requested Mouse control.");
		REQUESTS.add(request);
		return request;
	}

	public static class MouseRequest extends AbstractRequest {
		private final MouseAdapter adapter;
		private final String modId;

		public MouseRequest(MouseAdapter adapter) {
			this.adapter = adapter;
			modId = ASM.findOwner(adapter).getModId();
		}

		@Override
		protected void onAcquisition() {
			INSTANCE.onActive();
		}

		@Override
		public void onRelease() {
			INSTANCE.release();
		}
	}
}