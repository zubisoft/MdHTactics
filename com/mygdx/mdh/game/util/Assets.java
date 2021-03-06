package com.mygdx.mdh.game.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;


public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = "[Assets]";
    public static final Assets instance = new Assets();
    private AssetManager assetManager;

    public static Skin uiSkin = new Skin(Gdx.files.internal("core/assets/skin/uiskin.json"));

    static FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/skin/CantedComic.ttf"));
    public BitmapFont font15 ;

    public AssetFonts fonts;

    public Map<String, AssetCharacter> characters = new HashMap<String, AssetCharacter>();
    public Map<String, AtlasRegion> maps = new HashMap<String, AtlasRegion>();
    public Map<String, AtlasRegion> guiElements = new HashMap<String, AtlasRegion>();
    public Map<String, AtlasRegion> effects = new HashMap<String, AtlasRegion>();
    public Map<String, AtlasRegion> abilities = new HashMap<String, AtlasRegion>();

    public class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts() {
            // Create three fonts using libGdx's 15px bitmap font
            defaultSmall = new BitmapFont(Gdx.files.internal(Constants.TEXTURE_ATLAS_FONT), true);
            defaultNormal = new BitmapFont(Gdx.files.internal(Constants.TEXTURE_ATLAS_FONT), true);
            defaultBig = new BitmapFont(Gdx.files.internal(Constants.TEXTURE_ATLAS_FONT), true);

            // set font sizes
            /*defaultSmall.setScale(0.75f);
            defaultNormal.setScale(1.0f);
            defaultBig.setScale(2.0f);*/

            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 14;
            parameter.borderColor= Color.BLACK;
            parameter.borderWidth=0.2f;
            parameter.color = Color.WHITE;
            parameter.magFilter = Texture.TextureFilter.Linear;
            parameter.minFilter = Texture.TextureFilter.Linear;
            parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúñ";
            font15 = generator.generateFont(parameter);
            uiSkin.add("font15",font15);

            parameter.color = Color.BLACK;
            parameter.size = 14;
            font15  = generator.generateFont(parameter);
            uiSkin.add("handwritten_black",font15);

            uiSkin.get("default", Label.LabelStyle.class).font = font15;
            uiSkin.get("handwritten_black", Label.LabelStyle.class).font = font15;
            uiSkin.get("handwritten_black", TextArea.TextFieldStyle.class).font = font15;

            parameter.color = Color.BLACK;
            parameter.size = 25;
            font15  = generator.generateFont(parameter);
            uiSkin.add("handwritten_black_big",font15);

            TextField.TextFieldStyle aux = new TextField.TextFieldStyle(uiSkin.get("handwritten_black", TextArea.TextFieldStyle.class));

            parameter.color = Color.BLACK;
            parameter.size = 20;
            font15  = generator.generateFont(parameter);
            aux.font = font15;

            uiSkin.add("handwritten_black_big", aux, TextArea.TextFieldStyle.class);

            Label.LabelStyle aux2 = new Label.LabelStyle(uiSkin.get("handwritten_black", Label.LabelStyle.class));

            parameter.borderWidth=1;
            parameter.borderColor= Color.WHITE;
            parameter.color = Color.WHITE;
            font15  = generator.generateFont(parameter);
            aux2.font = font15;

            uiSkin.add("handwritten_white_big", aux2, Label.LabelStyle.class);

            parameter.color = Color.WHITE;
            parameter.size = 25;
            font15  = generator.generateFont(parameter);
            uiSkin.add("handwritten_white", font15);
        }
    }

    // Singleton: prevent initialization from other classes
    private Assets() {

    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_CHARACTERS, TextureAtlas.class);
        assetManager.load(Constants.TEXTURE_ATLAS_GUI, TextureAtlas.class);
        assetManager.load(Constants.TEXTURE_ATLAS_MAPS, TextureAtlas.class);
        assetManager.load(Constants.TEXTURE_ATLAS_EFFECTS, TextureAtlas.class);
        assetManager.load(Constants.TEXTURE_ATLAS_ABILITIES, TextureAtlas.class);

        // load sounds
        /*
        assetManager.load("sounds/jump.wav", Sound.class);
        assetManager.load("sounds/jump_with_feather.wav", Sound.class);
        assetManager.load("sounds/pickup_coin.wav", Sound.class);
        assetManager.load("sounds/pickup_feather.wav", Sound.class);
        assetManager.load("sounds/live_lost.wav", Sound.class);
        // load music
        assetManager.load("music/Maid_with_the_Flaxen_Hair.mp3",
                Music.class);
         */
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);


        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_CHARACTERS);


        // Enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures())
            t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        for (AtlasRegion g: atlas.getRegions()) {
            //LOG.print(10, "[Assets] Loading Character: " + g.name);
            characters.put(g.name, new AssetCharacter(atlas,g.name));
        }

        atlas = assetManager.get(Constants.TEXTURE_ATLAS_MAPS);
        for (AtlasRegion g: atlas.getRegions()) {
            //LOG.print(10, "[Assets] Loading Map: " + g.name);
            maps.put(g.name, atlas.findRegion(g.name));
        }

        atlas = assetManager.get(Constants.TEXTURE_ATLAS_GUI);
        for (AtlasRegion g: atlas.getRegions()) {
            //LOG.print(10, "[Assets] Loading GUI: " + g.name);
            guiElements.put(g.name, atlas.findRegion(g.name));
        }
        // Create gameScreen resource objects

        atlas = assetManager.get(Constants.TEXTURE_ATLAS_EFFECTS);
        for (AtlasRegion g: atlas.getRegions()) {
            //LOG.print(2, "[Assets] Loading effects: " + g.name);
            effects.put(g.name, atlas.findRegion(g.name));
        }

        atlas = assetManager.get(Constants.TEXTURE_ATLAS_ABILITIES);
        for (AtlasRegion g: atlas.getRegions()) {
            //LOG.print(2, "[Assets] Loading abilities: " + g.name);
            abilities.put(g.name, atlas.findRegion(g.name));
            effects.put("icons/"+g.name, atlas.findRegion(g.name));
        }


        fonts = new AssetFonts();
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.defaultBig.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultSmall.dispose();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "could not load asset '" + asset.fileName + "'", (Exception) throwable);
    }




    public class AssetCharacter {
        public final AtlasRegion character;
        public final Animation idleAnimation;
        public final Animation walkAnimation;
        public final Animation attackAnimation;
        public final Animation hitAnimation;
        public final TextureRegion portrait;

        private static final int        FRAME_COLS = 6;         // #1
        private static final int        FRAME_ROWS = 3;         // #2

        public AssetCharacter(TextureAtlas atlas, String characterName) {
            character = atlas.findRegion(characterName);


            TextureRegion tex = new TextureRegion(character ,0 ,0 ,500 ,500);


            TextureRegion[][] tmp = tex.split(83,125);
            TextureRegion[] frames = new TextureRegion[FRAME_COLS];


            for (int j = 0; j < FRAME_COLS; j++) {
                frames[j] = tmp[0][j];
            }

            walkAnimation = new Animation(0.15f, frames);      // #11


            frames = new TextureRegion[FRAME_COLS];

            for (int j = 0; j < FRAME_COLS; j++) {
                frames[j] = tmp[1][j];
            }

            attackAnimation = new Animation(0.15f, frames);      // #11

            frames = new TextureRegion[1];
            frames[0] = tmp[2][0];

            idleAnimation = new Animation(0.15f, frames);      // #11

            frames = new TextureRegion[1];
            frames[0] = tmp[2][1];
            hitAnimation = new Animation(0.15f, frames);      // #11

            portrait = new TextureRegion();
            portrait.setRegion(tmp[3][0]);
            portrait.setRegionWidth(83*2);






        }
    }
}