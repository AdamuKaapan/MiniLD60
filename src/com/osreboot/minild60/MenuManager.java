package com.osreboot.minild60;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.*;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.minild60.ControlManager.Action;
import com.osreboot.minild60.TextureManager.TextureSeries;
import com.osreboot.ridhvl.HvlFontUtil;
import com.osreboot.ridhvl.HvlTextureUtil;
import com.osreboot.ridhvl.config.HvlConfigUtil;
import com.osreboot.ridhvl.menu.HvlComponent;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox.ArrangementStyle;
import com.osreboot.ridhvl.menu.component.HvlButton;
import com.osreboot.ridhvl.menu.component.HvlCheckbox;
import com.osreboot.ridhvl.menu.component.HvlLabel;
import com.osreboot.ridhvl.menu.component.HvlListBox;
import com.osreboot.ridhvl.menu.component.HvlSlider;
import com.osreboot.ridhvl.menu.component.HvlSlider.SliderDirection;
import com.osreboot.ridhvl.menu.component.HvlSpacer;
import com.osreboot.ridhvl.menu.component.collection.HvlTextButton;
import com.osreboot.ridhvl.menu.component.collection.HvlTextureDrawable;
import com.osreboot.ridhvl.painter.HvlRenderFrame;
import com.osreboot.ridhvl.painter.HvlRenderFrame.HvlRenderFrameProfile;
import com.osreboot.ridhvl.painter.HvlShader;
import com.osreboot.ridhvl.painter.painter2d.HvlFontPainter2D;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;

public class MenuManager {

	public static HvlFontPainter2D font;

	public static HvlMenu main, levels, game, achievements, options, paused, win, splash;
	private static HvlArrangerBox mainArranger, levelArranger, achievementArranger, optionsArranger, pausedArranger, winArranger;
	private static HvlLabel mainTitle, achievementTitle, levelTitle, pausedTitle, winTitle, optionsTitle, optionsVolumeIndicator, optionsSoundIndicator;
	private static HvlTextButton mainPlay, mainAchievements, mainOptions, mainQuit, levelPlay, levelBack, achievementBack, optionsSave, optionsBack,
			pausedResume, pausedQuit, winQuit;
	private static HvlSlider optionsVolume, optionsSound;
	private static HvlListBox levelList, achievementsList;
	private static HvlCheckbox optionsLasers;
	private static HvlLabel achievementDescriptionLabel;
	private static HvlRenderFrame textFrame, barFrame;
	private static HvlShader textPost;

	public static void initialize() {
		font = new HvlFontPainter2D(TextureManager.getResource(TextureSeries.UI, 2), HvlFontUtil.DEFAULT, 2048, 2048, 192, 256, 10);

		textFrame = new HvlRenderFrame(HvlRenderFrameProfile.DEFAULT, Display.getWidth(), Display.getHeight());
		barFrame = new HvlRenderFrame(HvlRenderFrameProfile.DEFAULT, Display.getWidth(), Display.getHeight());
		textPost = new HvlShader(HvlShader.VERTEX_DEFAULT, "shader\\BarPost.hvlfg");

		/* SPLASH */
		splash = new HvlMenu() {
			@Override
			public void draw(float delta) {
				drawBackground();
				if (Main.getTotalTime() < 1) {
					HvlPainter2D.hvlDrawQuad(0, 0, 2048, 2048, TextureManager.getResource(TextureSeries.UI, 15));
				} else if (Main.getTotalTime() >= 1 && Main.getTotalTime() < 3) {
					HvlPainter2D.hvlDrawQuad(0, 0, 2048, 2048, TextureManager.getResource(TextureSeries.UI, 14));
					font.drawWord("wuballiance", Display.getWidth() / 64 * 19, Display.getHeight() / 32 * 26, 0.25f, Color.red);
				} else
					HvlMenu.setCurrent(main);
				super.draw(delta);
			}
		};
		/* END SPLASH */

		/* MAIN MENU */
		main = new HvlMenu() {
			@Override
			public void draw(float delta) {
				drawBackground();
				super.draw(delta);
			}
		};

		mainArranger = new HvlArrangerBox.Builder().setWidth(Display.getWidth()).setHeight(Display.getHeight()).setStyle(ArrangementStyle.VERTICAL)
				.setAlign(0.5f).build();
		main.add(mainArranger);

		mainTitle = new HvlLabel.Builder().setFont(font).setText("audiovisual extermination").setColor(Color.red).setScale(0.25f).build();
		mainArranger.add(mainTitle);

		mainArranger.add(getBlankSpace());
		// TODO: Replace with a builder and move event to update loop
		mainPlay = new HvlTextButton(0, 0, Display.getWidth() / 4 * 3, Display.getHeight() / 8, getButton(), new HvlTextureDrawable(TextureManager.getResource(
				TextureSeries.UI, 9)), font, "play") {
			@Override
			public void draw(float delta) {
				preDrawButtonFeatures(this, delta);
				super.draw(delta);
				postDrawButtonFeatures(this, delta);
			}

			@Override
			public void onTriggered() {
				AchievementManager.lookedAtLevelSelect = true;
				HvlMenu.setCurrent(levels);
			}
		};
		mainPlay.setTextScale(0.3f);
		mainArranger.add(mainPlay);

		mainArranger.add(getBlankSpace());
		// TODO: Replace with a builder and move event to update loop
		mainAchievements = new HvlTextButton(0, 0, Display.getWidth() / 4 * 3, Display.getHeight() / 8, getButton(), new HvlTextureDrawable(
				TextureManager.getResource(TextureSeries.UI, 9)), font, "achievements") {
			@Override
			public void draw(float delta) {
				preDrawButtonFeatures(this, delta);
				super.draw(delta);
				postDrawButtonFeatures(this, delta);
			}

			@Override
			public void onTriggered() {
				AchievementManager.lookedAtAchievements = true;
				achievementArranger.remove(achievementsList);
				achievementArranger.remove(achievementDescriptionLabel);
				achievementArranger.remove(achievementBack);

				// HvlSlider achievementSlider = new HvlSlider(0, 0,
				// Display.getWidth() / 8, Display.getHeight() / 2,
				// SliderDirection.VERTICAL, 64, 64, 0,
				// new
				// HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI,
				// 5)), new HvlTextureDrawable(TextureManager.getResource(
				// TextureSeries.UI, 4)));
				// achievementSlider.setHandleStartOffset(64);
				// achievementSlider.setHandleEndOffset(64);
				HvlSlider achievementSlider = new HvlSlider.Builder().setWidth(Display.getWidth() / 8).setHeight(Display.getHeight() / 2)
						.setDirection(SliderDirection.VERTICAL).setHandleWidth(64).setHandleHeight(64).setValue(0.0f)
						.setHandleUpDrawable(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 5)))
						.setHandleDownDrawable(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 5)))
						.setBackground(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 4))).setHandleStartOffset(64).setHandleEndOffset(64)
						.build();

				// achievementsList = new HvlListBox(0, 0, Display.getWidth() /
				// 4 * 3, Display.getHeight() / 2, achievementSlider, new
				// HvlButton(0, 0, 0, 0,
				// new HvlTextureDrawable(HvlTextureUtil.getColoredRect(1, 1,
				// Color.transparent)), new
				// HvlTextureDrawable(HvlTextureUtil.getColoredRect(1,
				// 1, Color.transparent))),
				// new HvlButton(0, 0, 0, 0, new
				// HvlTextureDrawable(HvlTextureUtil.getColoredRect(1, 1,
				// Color.transparent)), new HvlTextureDrawable(
				// HvlTextureUtil.getColoredRect(1, 1, Color.transparent))),
				// font, new HvlTextureDrawable(TextureManager.getResource(
				// TextureSeries.UI, 12)), new
				// HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI,
				// 13)), 64, 5) {
				// };
				// achievementsList.setTextScale(0.15f);
				achievementsList = new HvlListBox.Builder().setWidth(Display.getWidth() / 4 * 3).setHeight(Display.getHeight() / 2)
						.setScrollBar(achievementSlider).setFont(font)
						.setItemBackgroundOff(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 12)))
						.setItemBackgroundHover(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 12)))
						.setItemBackgroundOn(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 13))).setTextScale(0.15f).build();

				achievementArranger.add(achievementsList);
				achievementArranger.add(achievementDescriptionLabel);
				achievementArranger.add(achievementBack);

				for (Achievement a : AchievementManager.getUnlockedAchievements()) {
					achievementsList.addItem(a);
				}
				HvlMenu.setCurrent(achievements);
			}
		};
		mainAchievements.setTextScale(0.3f);
		mainArranger.add(mainAchievements);

		mainArranger.add(getBlankSpace());
		mainOptions = new HvlTextButton(0, 0, Display.getWidth() / 4 * 3, Display.getHeight() / 8, getButton(), new HvlTextureDrawable(
				TextureManager.getResource(TextureSeries.UI, 9)), font, "options") {
			@Override
			public void draw(float delta) {
				preDrawButtonFeatures(this, delta);
				super.draw(delta);
				postDrawButtonFeatures(this, delta);
			}

			@Override
			public void onTriggered() {
				AchievementManager.lookedAtOptions = true;
				loadOptions();
				HvlMenu.setCurrent(options);
			}
		};
		mainOptions.setTextScale(0.3f);
		mainArranger.add(mainOptions);

		mainArranger.add(getBlankSpace());
		mainQuit = new HvlTextButton(0, 0, Display.getWidth() / 4 * 3, Display.getHeight() / 8, getButton(), new HvlTextureDrawable(TextureManager.getResource(
				TextureSeries.UI, 9)), font, "quit") {
			@Override
			public void draw(float delta) {
				preDrawButtonFeatures(this, delta);
				super.draw(delta);
				postDrawButtonFeatures(this, delta);
			}

			@Override
			public void onTriggered() {
				System.exit(0);
			}
		};
		mainQuit.setTextScale(0.3f);
		mainArranger.add(mainQuit);
		/* END MAIN MENU */

		/* LEVEL SELECT */
		levels = new HvlMenu() {
			@Override
			public void draw(float delta) {
				drawBackground();
				super.draw(delta);
			}
		};

		levelArranger = new HvlArrangerBox.Builder().setWidth(Display.getWidth()).setHeight(Display.getHeight()).setStyle(ArrangementStyle.VERTICAL)
				.setAlign(0.5f).build();
		levels.add(levelArranger);

		levelTitle = new HvlLabel.Builder().setFont(font).setText("level select").setColor(Color.red).setScale(0.25f).build();
		levelArranger.add(levelTitle);

		levelArranger.add(getBlankSpace(Display.getHeight() / 64));
		HvlLabel levelAchCount = new HvlLabel(0, 0, font, "you have ." + "( achievements", Color.red, 0.25f) {
			@Override
			public void draw(float delta) {
				super.setText("you have " + AchievementManager.getNumberUnlocked() + " achievements");
				super.draw(delta);
			}
		};
		levelAchCount.setScale(0.1f);
		levelArranger.add(levelAchCount);

		HvlSlider levelSlider = new HvlSlider.Builder().setWidth(Display.getWidth() / 8).setHeight(Display.getHeight() / 2)
				.setDirection(SliderDirection.VERTICAL).setHandleWidth(64).setHandleHeight(64)
				.setHandleDownDrawable(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 5)))
				.setHandleUpDrawable(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 5)))
				.setBackground(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 4))).setHandleStartOffset(64).setHandleEndOffset(64).build();

		levelArranger.add(getBlankSpace(Display.getHeight() / 64));
		levelList = new HvlListBox.Builder().setWidth(Display.getWidth() / 4 * 3).setHeight(Display.getHeight() / 2).setScrollBar(levelSlider)
				.setFont(font).setItemBackgroundOff(new
		 HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 12))).setItemBackgroundHover(new
		 HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 12))).setItemBackgroundOn(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI,
					 13))).setItemHeight(64).setMaxVisibleItems(5).setTextScale(0.25f).build();
		for (Level level : Level.levels) {
			levelList.addItem(Level.levels.indexOf(level)
					+ (level.getRequiredAchievements() - AchievementManager.getNumberUnlocked() > 0 ? " .req "
							+ (level.getRequiredAchievements() - AchievementManager.getNumberUnlocked()) + " more(" : " unlocked"));
		}
		levelArranger.add(levelList);

		levelArranger.add(getBlankSpace());
		levelPlay = new HvlTextButton(0, 0, Display.getWidth() / 4 * 3, Display.getHeight() / 8, getButton(), new HvlTextureDrawable(
				TextureManager.getResource(TextureSeries.UI, 9)), font, "play") {
			@Override
			public void draw(float delta) {
				preDrawButtonFeatures(this, delta);
				super.draw(delta);
				postDrawButtonFeatures(this, delta);
			}

			@Override
			public void onTriggered() {
				if (AchievementManager.getNumberUnlocked() >= (levelList.getSelectedIndex() == -1 ? Level.levels.get(0) : Level.levels.get(levelList
						.getSelectedIndex())).getRequiredAchievements()) {
					Game.currentLevel = levelList.getSelectedIndex() == -1 ? Level.levels.get(0) : Level.levels.get(levelList.getSelectedIndex());
					HvlMenu.setCurrent(game);
					Game.initialize();
					Game.reset();
				}
			}
		};
		levelPlay.setTextScale(0.3f);
		levelArranger.add(levelPlay);

		levelArranger.add(getBlankSpace());
		levelBack = new HvlTextButton(0, 0, Display.getWidth() / 4 * 3, Display.getHeight() / 8, getButton(), new HvlTextureDrawable(
				TextureManager.getResource(TextureSeries.UI, 9)), font, "back") {
			@Override
			public void draw(float delta) {
				preDrawButtonFeatures(this, delta);
				super.draw(delta);
				postDrawButtonFeatures(this, delta);
			}

			@Override
			public void onTriggered() {
				HvlMenu.setCurrent(main);
			}
		};
		levelBack.setTextScale(0.3f);
		levelArranger.add(levelBack);
		/* END LEVEL SELECT */

		/* ACHIEVEMENTS */
		achievements = new HvlMenu() {
			@Override
			public void draw(float delta) {
				if (achievementsList.getSelectedItem() != null) {
					Achievement ach = (Achievement) achievementsList.getSelectedItem();
					achievementDescriptionLabel.setText(ach.getDescription().toLowerCase());
				}

				drawBackground();
				super.draw(delta);
			}
		};

		achievementArranger = new HvlArrangerBox.Builder().setWidth(Display.getWidth()).setHeight(Display.getHeight()).setStyle(ArrangementStyle.VERTICAL)
				.build();
		achievements.add(achievementArranger);

		achievementTitle = new HvlLabel.Builder().setFont(font).setText("achievements").setColor(Color.red).setScale(0.25f).build();
		achievementArranger.add(achievementTitle);

		HvlSlider achievementSlider = new HvlSlider.Builder().setWidth(Display.getWidth() / 8).setHeight(Display.getHeight() / 2)
				.setDirection(SliderDirection.VERTICAL).setHandleWidth(64).setHandleHeight(64)
				.setHandleDownDrawable(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 5)))
				.setHandleUpDrawable(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 5)))
				.setBackground(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 4))).setHandleStartOffset(64).setHandleEndOffset(64).build();

		// achievementsList = new HvlListBox(0, 0, Display.getWidth() / 4 * 3,
		// Display.getHeight() / 2, achievementSlider, new HvlButton(0, 0, 0, 0,
		// new HvlTextureDrawable(HvlTextureUtil.getColoredRect(1, 1,
		// Color.transparent)), new
		// HvlTextureDrawable(HvlTextureUtil.getColoredRect(1, 1,
		// Color.transparent))), new HvlButton(0, 0, 0, 0, new
		// HvlTextureDrawable(HvlTextureUtil.getColoredRect(1, 1,
		// Color.transparent)),
		// new HvlTextureDrawable(HvlTextureUtil.getColoredRect(1, 1,
		// Color.transparent))), font, new
		// HvlTextureDrawable(TextureManager.getResource(
		// TextureSeries.UI, 12)), new
		// HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 13)),
		// 64, 5) {
		// @Override
		// public void onSelectionChanged(int indexArg, Object selArg) {
		// if (selArg == null)
		// return;
		//
		// Achievement ach = (Achievement) selArg;
		// achievementDescriptionLabel.setText(ach.getDescription().toLowerCase());
		// }
		// };
		// achievementsList.setTextScale(0.25f);
		achievementsList = new HvlListBox.Builder().setWidth(Display.getWidth() / 4 * 3).setHeight(Display.getHeight() / 2).setScrollBar(achievementSlider)
				.setFont(font).setItemBackgroundOff(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 12)))
				.setItemBackgroundHover(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 12)))
				.setItemBackgroundOn(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 13))).setTextScale(0.15f).build();
		achievementArranger.add(achievementsList);

		for (Achievement a : AchievementManager.getUnlockedAchievements()) {
			achievementsList.addItem(a);
		}

		achievementDescriptionLabel = new HvlLabel.Builder().setFont(font).setText("").setColor(Color.white).setScale(0.2f).build();
		achievementArranger.add(achievementDescriptionLabel);

		achievementArranger.add(getBlankSpace());
		achievementBack = new HvlTextButton(0, 0, Display.getWidth() / 4 * 3, Display.getHeight() / 8, getButton(), new HvlTextureDrawable(
				TextureManager.getResource(TextureSeries.UI, 9)), font, "back") {
			@Override
			public void draw(float delta) {
				preDrawButtonFeatures(this, delta);
				super.draw(delta);
				postDrawButtonFeatures(this, delta);
			}

			@Override
			public void onTriggered() {
				HvlMenu.setCurrent(main);
			}
		};
		achievementBack.setTextScale(0.3f);
		achievementArranger.add(achievementBack);
		/* END ACHIEVEMENTS */

		/* OPTIONS */
		options = new HvlMenu() {
			@Override
			public void draw(float delta) {
				drawBackground();
				super.draw(delta);
			}
		};

		optionsArranger = new HvlArrangerBox.Builder().setWidth(Display.getWidth()).setHeight(Display.getHeight()).setStyle(ArrangementStyle.VERTICAL)
				.setAlign(0.5f).build();
		options.add(optionsArranger);

		optionsTitle = new HvlLabel.Builder().setFont(font).setText("options").setColor(Color.red).setScale(0.25f).build();
		optionsArranger.add(optionsTitle);

		optionsArranger.add(getBlankSpace());
		optionsVolumeIndicator = new HvlLabel(0, 0, font, "music volume ", Color.red, 0.25f) {
			@Override
			public void draw(float delta) {
				setText("music volume " + (int) (optionsVolume.getValue() * 100));
				super.draw(delta);
			}
		};
		optionsArranger.add(optionsVolumeIndicator);

		optionsVolume = new HvlSlider.Builder().setWidth(Display.getWidth() / 2).setHeight(Display.getHeight() / 8).setDirection(SliderDirection.HORIZONTAL)
				.setHandleWidth(64).setHandleHeight(64).setValue(1.0f)
				.setHandleDownDrawable(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 7)))
				.setHandleUpDrawable(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 7)))
				.setBackground(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 8))).setHandleStartOffset(64).setHandleEndOffset(64)
				.setSnapInterval(0.01f).setValue(OptionsConfig.volume).build();
		optionsArranger.add(optionsVolume);

		optionsArranger.add(getBlankSpace());
		optionsSoundIndicator = new HvlLabel(0, 0, font, "effects volume ", Color.red, 0.25f) {
			@Override
			public void draw(float delta) {
				setText("effects volume " + (int) (optionsSound.getValue() * 100));
				super.draw(delta);
			}
		};
		optionsArranger.add(optionsSoundIndicator);

		// optionsSound = new HvlSlider(0, 0, Display.getWidth() / 2,
		// Display.getHeight() / 8, SliderDirection.HORIZONTAL, 64, 64, 1, new
		// HvlTextureDrawable(
		// TextureManager.getResource(TextureSeries.UI, 7)), new
		// HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 8)));
		// optionsSound.setHandleStartOffset(64);
		// optionsSound.setHandleEndOffset(64);
		// optionsSound.setSnapInterval(0.01f);
		// optionsSound.setValue(OptionsConfig.sound);
		optionsSound = new HvlSlider.Builder().setWidth(Display.getWidth() / 2).setHeight(Display.getHeight() / 8).setDirection(SliderDirection.HORIZONTAL)
				.setHandleWidth(64).setHandleHeight(64).setValue(1.0f)
				.setHandleDownDrawable(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 7)))
				.setHandleUpDrawable(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 7)))
				.setBackground(new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 8))).setHandleStartOffset(64).setHandleEndOffset(64)
				.setSnapInterval(0.01f).setValue(OptionsConfig.sound).build();
		optionsArranger.add(optionsSound);

		optionsArranger.add(new HvlSpacer(Display.getWidth(), (Display.getHeight() / 16) + 32));

		optionsLasers = new HvlCheckbox(Display.getWidth() / 8 * 7, Display.getHeight() / 8 * 5, 32, 32, false, new HvlTextureDrawable(
				TextureManager.getResource(TextureSeries.UI, 11)), new HvlTextureDrawable(TextureManager.getResource(TextureSeries.UI, 10))) {
			@Override
			public void draw(float delta) {
				font.drawWord("show laser .shader bugs(", super.getX() - Display.getWidth() / 8 * 6, super.getY() - Display.getHeight() / 64, 0.2f, Color.red);
				super.draw(delta);
			}
		};
		optionsLasers.setChecked(OptionsConfig.linesVisible);
		options.add(optionsLasers);

		optionsArranger.add(getBlankSpace());
		optionsSave = new HvlTextButton(0, 0, Display.getWidth() / 4 * 3, Display.getHeight() / 8, getButton(), new HvlTextureDrawable(
				TextureManager.getResource(TextureSeries.UI, 9)), font, "save") {
			@Override
			public void draw(float delta) {
				preDrawButtonFeatures(this, delta);
				super.draw(delta);
				postDrawButtonFeatures(this, delta);
			}

			@Override
			public void onTriggered() {
				saveOptions();
			}
		};
		optionsSave.setTextScale(0.3f);
		optionsArranger.add(optionsSave);

		optionsArranger.add(getBlankSpace());
		optionsBack = new HvlTextButton(0, 0, Display.getWidth() / 4 * 3, Display.getHeight() / 8, getButton(), new HvlTextureDrawable(
				TextureManager.getResource(TextureSeries.UI, 9)), font, "back") {
			@Override
			public void draw(float delta) {
				preDrawButtonFeatures(this, delta);
				super.draw(delta);
				postDrawButtonFeatures(this, delta);
			}

			@Override
			public void onTriggered() {
				HvlConfigUtil.loadStaticConfig(OptionsConfig.class, "res\\options.txt");
				HvlMenu.setCurrent(main);
			}
		};
		optionsBack.setTextScale(0.3f);
		optionsArranger.add(optionsBack);
		/* END OPTIONS */

		/* PAUSED */
		paused = new HvlMenu() {
			@Override
			public void draw(float delta) {
				drawBackground();
				super.draw(delta);
			}
		};

		pausedArranger = new HvlArrangerBox.Builder().setWidth(Display.getWidth()).setHeight(Display.getHeight()).setStyle(ArrangementStyle.VERTICAL)
				.setAlign(0.5f).build();
		paused.add(pausedArranger);

		pausedTitle = new HvlLabel.Builder().setFont(font).setText("paused").setColor(Color.red).setScale(0.25f).build();
		pausedArranger.add(pausedTitle);

		pausedArranger.add(getBlankSpace());
		pausedResume = new HvlTextButton(0, 0, Display.getWidth() / 4 * 3, Display.getHeight() / 8, getButton(), new HvlTextureDrawable(
				TextureManager.getResource(TextureSeries.UI, 9)), font, "resume") {
			@Override
			public void draw(float delta) {
				preDrawButtonFeatures(this, delta);
				super.draw(delta);
				postDrawButtonFeatures(this, delta);
			}

			@Override
			public void onTriggered() {
				HvlMenu.setCurrent(game);
			}
		};
		pausedResume.setTextScale(0.3f);
		pausedArranger.add(pausedResume);

		pausedArranger.add(getBlankSpace());
		pausedQuit = new HvlTextButton(0, 0, Display.getWidth() / 4 * 3, Display.getHeight() / 8, getButton(), new HvlTextureDrawable(
				TextureManager.getResource(TextureSeries.UI, 9)), font, "quit") {
			@Override
			public void draw(float delta) {
				preDrawButtonFeatures(this, delta);
				super.draw(delta);
				postDrawButtonFeatures(this, delta);
			}

			@Override
			public void onTriggered() {
				HvlMenu.setCurrent(main);
			}
		};
		pausedQuit.setTextScale(0.3f);
		pausedArranger.add(pausedQuit);
		/* END PAUSED */

		/* IN-GAME */
		game = new HvlMenu() {
			@Override
			public void draw(float delta) {
				Game.update(delta);
				if (ControlManager.isActionTriggering(Action.PAUSE))
					HvlMenu.setCurrent(paused);
			}
		};
		/* END IN-GAME */

		/* WIN */
		win = new HvlMenu() {
			@Override
			public void draw(float delta) {
				drawBackground();
				super.draw(delta);
			}
		};

		winArranger = new HvlArrangerBox.Builder().setWidth(Display.getWidth()).setHeight(Display.getHeight()).setStyle(ArrangementStyle.VERTICAL).setAlign(0.5f).build();
		win.add(winArranger);

		winTitle = new HvlLabel.Builder().setFont(font).setText("level cleared!").setColor(Color.red).setScale(0.25f).build();
		winArranger.add(winTitle);

		winArranger.add(getBlankSpace());
		winQuit = new HvlTextButton(0, 0, Display.getWidth() / 4 * 3, Display.getHeight() / 8, getButton(), new HvlTextureDrawable(TextureManager.getResource(
				TextureSeries.UI, 9)), font, "level select") {
			@Override
			public void draw(float delta) {
				preDrawButtonFeatures(this, delta);
				super.draw(delta);
				postDrawButtonFeatures(this, delta);
			}

			@Override
			public void onTriggered() {
				HvlMenu.setCurrent(levels);
			}
		};
		winQuit.setTextScale(0.3f);
		winArranger.add(winQuit);
		/* END WIN */

		loadOptions();

		HvlMenu.setCurrent(splash);
	}

	public static void postUpdate(float delta) {
		if (HvlMenu.getCurrent() != game) {
			HvlShader.setCurrentShader(textPost);
			textPost.sendRenderFrame("texture2", 2, barFrame);

			hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(), textFrame);

			HvlShader.setCurrentShader(null);

			HvlRenderFrame.setCurrentRenderFrame(textFrame, true);
			HvlRenderFrame.setCurrentRenderFrame(null);
			HvlRenderFrame.setCurrentRenderFrame(barFrame, true);
			HvlRenderFrame.setCurrentRenderFrame(null);
		}
	}

	public static void preDrawButtonFeatures(HvlComponent component, float delta) {
		HvlRenderFrame.setCurrentRenderFrame(barFrame, false);
		HvlPainter2D.hvlDrawQuad(component.getX(), component.getY(), component.getWidth(), component.getHeight(),
				TextureManager.getResource(TextureSeries.UI, 0), new Color(0.1f, 0.1f, 0));
		HvlPainter2D.hvlDrawQuad(component.getX(), component.getY(),
				component.getWidth() * Math.max((float) Math.sin((total + (component.getY() / (float) Display.getHeight())) * 2), 0), component.getHeight(),
				TextureManager.getResource(TextureSeries.UI, 0), Color.yellow);
		HvlRenderFrame.setCurrentRenderFrame(null);

		HvlPainter2D.hvlDrawQuad(component.getX(), component.getY(), component.getWidth(), component.getHeight(),
				TextureManager.getResource(TextureSeries.UI, 0), new Color(0.1f, 0.1f, 0));
		HvlPainter2D.hvlDrawQuad(component.getX(), component.getY(),
				component.getWidth() * Math.max((float) Math.sin((total + (component.getY() / (float) Display.getHeight())) * 2), 0), component.getHeight(),
				TextureManager.getResource(TextureSeries.UI, 0), Color.yellow);

		HvlRenderFrame.setCurrentRenderFrame(textFrame, false);
	}

	public static void postDrawButtonFeatures(HvlComponent component, float delta) {
		HvlRenderFrame.setCurrentRenderFrame(null);
		HvlPainter2D.hvlDrawQuad(component.getX(), component.getY(), component.getWidth(), component.getHeight(),
				TextureManager.getResource(TextureSeries.UI, 3));
	}

	public static HvlSpacer getBlankSpace() {
		return new HvlSpacer(Display.getWidth(), Display.getHeight() / 32);
	}

	public static HvlSpacer getBlankSpace(float space) {
		return new HvlSpacer(Display.getWidth(), space);
	}

	public static HvlTextureDrawable getButton() {
		return new HvlTextureDrawable(HvlTextureUtil.getColoredRect(1, 1, Color.transparent));
		// return new HvlTiledRectDrawable(new
		// HvlTiledRect(TextureManager.getResource(TextureSeries.UI, 3), 0.45f,
		// 0.55f, 0.45f, 0.55f, 0, 0, 0, 0, 64, 64));
	}

	private static float total = 0;

	public static void update(float delta) {
		total += delta;
	}

	public static void drawBackground() {
		HvlPainter2D.hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(), TextureManager.getResource(TextureSeries.UI, 0), Color.darkGray);
	}

	private static void loadOptions() {
		HvlConfigUtil.loadStaticConfig(OptionsConfig.class, "res\\options.txt");
		optionsVolume.setValue(OptionsConfig.volume);
		optionsSound.setValue(OptionsConfig.sound);
		optionsLasers.setChecked(OptionsConfig.linesVisible);
	}

	private static void saveOptions() {
		OptionsConfig.volume = optionsVolume.getValue();
		OptionsConfig.sound = optionsSound.getValue();
		OptionsConfig.linesVisible = optionsLasers.getChecked();
		HvlConfigUtil.saveStaticConfig(OptionsConfig.class, "res\\options.txt");
		SoundManager.reset();
	}

}
