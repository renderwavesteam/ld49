package com.renderwaves.ld49.entity.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.renderwaves.ld49.GlobalShipVariables;
import com.renderwaves.ld49.entity.TexturedEntity;
import com.renderwaves.ld49.managers.FontManager;
import com.renderwaves.ld49.managers.InputManager;
import com.renderwaves.ld49.managers.TextureManager;
import com.renderwaves.ld49.scenes.TemplateScene;
import com.renderwaves.ld49.tilemap.Tile;

import static com.renderwaves.ld49.Game.entityManager;

public class PlayerEntity extends TexturedEntity {

    private String TAG = PlayerEntity.class.getName();

    private float movementSpeed = 100.0f;
    private float sprint = 1.0f;
    private Vector2 velocity;
    private boolean nearGenerator = false;
    private boolean nearLifeSupport = false;
    private boolean nearMedBay = false;
    private boolean hasSpacesuit = false;
    private boolean nearSpacesuit = false;

    /*
     */
    public PlayerEntity(Vector2 position, Vector2 scale) {
        super(position, scale, TextureManager.playerEntity);
        velocity = new Vector2(0, 0);

        rectangle = new Rectangle(position.x, position.y, texture.getWidth() * scale.x, texture.getHeight() * scale.y);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);

        if(nearGenerator) {
            FontManager.font_droidBb_20.draw(spriteBatch, "ADDING FUEL TO GENERATOR", Gdx.graphics.getWidth() / 2 - "ADDING FUEL TO GENERATOR".length() * 7, 100);
            GlobalShipVariables.shipHealth += Gdx.graphics.getDeltaTime() / 2;
        }
        else if(nearLifeSupport) {
            FontManager.font_droidBb_20.draw(spriteBatch, "REPAIRING LIFE SUPPORT", Gdx.graphics.getWidth() / 2 - "REPAIRING LIFE SUPPORT".length() * 7, 100);
            GlobalShipVariables.shipHealth += Gdx.graphics.getDeltaTime() / 2;
        }
        else if(nearMedBay) {
            FontManager.font_droidBb_20.draw(spriteBatch, "HEALING", Gdx.graphics.getWidth() / 2 - "HEALING".length() * 7, 100);
            GlobalShipVariables.shipHealth += Gdx.graphics.getDeltaTime() / 2;
        }
        else if(nearSpacesuit) {
            FontManager.font_droidBb_20.draw(spriteBatch, (hasSpacesuit ? "PUT BACK" : "TAKE") + " SPACESUIT <" + Input.Keys.toString(InputManager.TakeSpacesuit.key1) + ">", Gdx.graphics.getWidth() / 2 - "ADDING FUEL TO GENERATOR".length() * 7, 100);
        }
    }

    private void movement() {
        rectangle.x = position.x;
        rectangle.y = position.y;

        if(Gdx.input.isKeyPressed(InputManager.Sprint.key1) || Gdx.input.isKeyPressed(InputManager.Sprint.key2)) {
            sprint = 2.0f;
        }

        if(Gdx.input.isKeyPressed(InputManager.MoveUp.key1) || Gdx.input.isKeyPressed(InputManager.MoveUp.key2)) {
            velocity.y = Gdx.graphics.getDeltaTime() * movementSpeed;
        }
        else if(Gdx.input.isKeyPressed(InputManager.MoveDown.key1) || Gdx.input.isKeyPressed(InputManager.MoveDown.key2)) {
            velocity.y = -Gdx.graphics.getDeltaTime() * movementSpeed;
        }
        if(Gdx.input.isKeyPressed(InputManager.MoveRight.key1) || Gdx.input.isKeyPressed(InputManager.MoveRight.key2)) {
            velocity.x = Gdx.graphics.getDeltaTime() * movementSpeed;
        }
        else if(Gdx.input.isKeyPressed(InputManager.MoveLeft.key1) || Gdx.input.isKeyPressed(InputManager.MoveLeft.key2)) {
            velocity.x = -Gdx.graphics.getDeltaTime() * movementSpeed;
        }

        Vector2 playerPositionOnTilemap = TemplateScene.shipTilemap.globalPositionToTilemapPosition(position.x+8 + velocity.x, position.y-4 + velocity.y);
        int tileID = TemplateScene.shipTilemap.getTileByPosition((int)playerPositionOnTilemap.x, (int)playerPositionOnTilemap.y).tileID;
        if(tileID == Tile.Air.tileID|| tileID == Tile.WallTile.tileID) {
            velocity.x = 0;
            velocity.y = 0;
        }

        position.x += velocity.x * sprint;
        position.y += velocity.y * sprint;

        velocity.x = 0;
        velocity.y = 0;
    }

    private Generator generator;
    private Spacesuit spacesuit;
    private LifeSupport lifeSupport;
    private MedBay medBay;

    private void collision() {
        if(generator == null && spacesuit == null) {
            for(int i = 0; i < entityManager.size(); i++) {
                if(entityManager.get(i) instanceof Generator) {
                    generator = (Generator) entityManager.get(i);
                }
                else if(entityManager.get(i) instanceof Spacesuit) {
                    spacesuit = (Spacesuit) entityManager.get(i);
                }
                else if(entityManager.get(i) instanceof  LifeSupport){
                    lifeSupport = (LifeSupport) entityManager.get(i);
                 }
                else if(entityManager.get(i) instanceof  MedBay){
                    medBay = (MedBay) entityManager.get(i);
                 }
            }
        }
        else {
            if(generator.rectangle.overlaps(rectangle)) {
                nearGenerator = true;
            }
            else {
                nearGenerator = false;
            }

            if(spacesuit.rectangle.overlaps(rectangle)) {
                nearSpacesuit = true;
            }
            else {
                nearSpacesuit = false;
            }

            if(lifeSupport.rectangle.overlaps(rectangle)){
                nearLifeSupport = true;
            }
            else{
                nearLifeSupport = false;
            }

            if(medBay.rectangle.overlaps(rectangle)){
                nearMedBay = true;
            }
            else{
                nearMedBay = false;
            }
        }
    }

    @Override
    public void update() {
        super.update();
        movement();
        collision();

        if(nearSpacesuit) {
            if(Gdx.input.isKeyJustPressed(InputManager.TakeSpacesuit.key1)) {
                hasSpacesuit = !hasSpacesuit;
                spacesuit.spacesuitTaken = hasSpacesuit;
                if(hasSpacesuit) {
                    sprite.setTexture(TextureManager.spacesuitTexture);
                }
                else {
                    sprite.setTexture(TextureManager.playerEntity);
                }
            }
        }

        sprint = 1.0f;
    }
}