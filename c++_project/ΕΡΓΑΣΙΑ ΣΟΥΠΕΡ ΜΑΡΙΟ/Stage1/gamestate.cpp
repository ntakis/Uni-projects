#include "gamestate.h"
#include "level.h"
#include "util.h"
#include "player.h"
#include <thread>
#include <chrono>


using namespace std::chrono_literals;

GameState::GameState()
{
}

GameState::~GameState()
{
	if (m_current_level)
		delete m_current_level;
}

GameState* GameState::getInstance()
{
	if (!m_unique_instance)
	{
		m_unique_instance = new GameState();
	}
	return m_unique_instance;
}

bool GameState::init()
{
	if (m_current_level == 0) {
		m_current_level = new Level("1.lvl");
		m_current_level->init();

		m_player = new Player("Player");
		m_player->init();

		graphics::preloadBitmaps(getAssetDir());
		graphics::setFont(m_asset_path + "OpenSans-Regular.ttf");
	}
	

	

	return true;
}

void GameState::draw()
{
	if (state == START)
	{
		//Initializing the brush
		graphics::Brush br;

		//Drawing the background
		br.texture = string(AP) + "back2.png";
		br.outline_opacity = 0.0f;
		graphics::drawRect(m_canvas_width / 2, m_canvas_height / 2, m_canvas_width, m_canvas_height, br);
		// Display a message on the screen
		

		if (graphics::getKeyState(graphics::SCANCODE_SPACE))
		{
			state = PLAY;
		}
		
	}

	else if (state == LOADING)
	{
		//graphics::Brush br2;
		//graphics::drawText(m_canvas_width / 2 - 3.0f, m_canvas_height / 2, 1.5f, "LOADING...", br2);
		
		state = PLAY;
		
		return;
	}

	else if (state == PLAY)
	{
		
		m_current_level->draw();
		
	}
}
void GameState::update(float dt)
{
	// Skip an update if a long delay is detected 
	// to avoid messing up the collision simulation
	if (dt > 500) // ms
		return;

	// Avoid too quick updates
	float sleep_time = std::max(17.0f - dt, 0.0f);
	if (sleep_time > 0.0f)
	{
		std::this_thread::sleep_for(std::chrono::duration<float, std::milli>(sleep_time));

	}
	/*graphics::drawText(getCanvasWidth() / 25, getCanvasHeight(), 1.0f, "Press H for help", m_text);
	if (graphics::getKeyState(graphics::SCANCODE_H))
	{
		graphics::drawText(getCanvasWidth() / 25, getCanvasHeight() / 2, 1.0f, "Press M to stop the music", m_text);
		graphics::drawText(getCanvasWidth() / 25, getCanvasHeight() / 2 + 20, 1.0f, "Reach the top to win", m_text);
	}*/

	if (getKeyState(graphics::SCANCODE_M))
	{
		graphics::stopMusic();
	}

	if (state == START)
	{
		graphics::drawText(m_canvas_width / 2 - 2.2f, m_canvas_height / 2 + 2, 0.5f, "Press 'SPACE' to start", m_text);
		graphics::drawText(m_canvas_width / 2 - 4.5f, m_canvas_height / 2 - 4.5f, 0.2f, "Press 'R' for game rules", m_text);
		return;
	}
	else if (state == LOADING)
	{
		
		init();
		
		return;
	}
	else if (state == PLAY)
	{   
		
		
		if (!m_current_level)
			return;

		m_current_level->update(dt);
	}


	

	m_debugging = graphics::getKeyState(graphics::SCANCODE_0);


}

std::string GameState::getFullAssetPath(const std::string& asset)
{
	return m_asset_path + asset;
}

std::string GameState::getAssetDir()
{
	return m_asset_path;
}

GameState* GameState::m_unique_instance = nullptr;