#include <sgg/graphics.h>
#include <string>

#include "gamestate.h"
#include "util.h"

void draw()
{
    GameState::getInstance()->draw();
}

void update(float dt)
{
    GameState::getInstance()->update(dt);
}


int main(int argc, char** argv)
{
    graphics::createWindow(1000, 800, "RedBall SkyJump");

    GameState::getInstance()->init();

    graphics::setDrawFunction(draw);
    graphics::setUpdateFunction(update);

    graphics::setCanvasSize(GameState::getInstance()->getCanvasWidth(), GameState::getInstance()->getCanvasHeight());
    graphics::setCanvasScaleMode(graphics::CANVAS_SCALE_FIT);
    //graphics::playMusic(string(AP) + "oaed.mp3", 0.6f, true, 0); //play music


    graphics::startMessageLoop();
    return 0;
}

