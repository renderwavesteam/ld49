package com.renderwaves.ld49.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.renderwaves.ld49.GlobalShipVariables;
import com.renderwaves.ld49.entity.entities.PlayerEntity;
import com.renderwaves.ld49.managers.FontManager;

public class YouWonScene implements Screen {
    private Stage stage;
    private Table table;
    private Table creditsTable;

    Game game;

    public YouWonScene(Game game){
        this.game = game;
        Skin uiSkin = new Skin();
        uiSkin.add("default-font", FontManager.font_droidBb_20);

        uiSkin.addRegions(new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
        uiSkin.load(Gdx.files.internal("skins/skin.json"));

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);

        stage.addActor(table);
        //table.setDebug(true);

        Label.LabelStyle gameNameStyle = new Label.LabelStyle();
        gameNameStyle.font = FontManager.font_droidBb_40;
        gameNameStyle.fontColor = new Color(1,1,1,1);

        final Label winText = new Label("YOU WON!", gameNameStyle);
        final TextButton restartButton = new TextButton("Restart", uiSkin);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                super.clicked(event, x, y);
                callbackStartGame();
            }
        });

        final TextButton quitButton = new TextButton("Quit", uiSkin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                super.clicked(event, x, y);
                callbackQuit();
            }
        });

        table.add(winText);
        table.row();
        table.add(restartButton).spaceTop(20).width(100).height(30);
        table.row();
        table.add(quitButton).spaceTop(5).width(100).height(30);
    }

    public void callbackStartGame(){
        TemplateScene.shipTilemap.doorHandler.clear();
        TemplateScene.shipTilemap.fireHandler.clear();
        TemplateScene.getInstance().gameEventSystem.completeAllEvents();
        TemplateScene.warningLabels.clear();
        com.renderwaves.ld49.Game.entityManager.clear();
        PlayerEntity.health = 1.0f;
        GlobalShipVariables.engine1Health = 1.0f;
        GlobalShipVariables.oxygenLevel = 1.0f;
        GlobalShipVariables.shipHealth = 1.0f;
        GlobalShipVariables.communicationsHealth = 1.0f;
        GlobalShipVariables.navigationHealth = 1.0f;
        GlobalShipVariables.lifeSupportHealth = 1.0f;
        GlobalShipVariables.generatorFuel = 1.0f;
        GlobalShipVariables.generatorHealth = 1.0f;
        GlobalShipVariables.engine2Health = 1.0f;

        this.game.setScreen(new TemplateScene((com.renderwaves.ld49.Game) game));
    }

    public void callbackQuit(){
        Gdx.app.exit();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
