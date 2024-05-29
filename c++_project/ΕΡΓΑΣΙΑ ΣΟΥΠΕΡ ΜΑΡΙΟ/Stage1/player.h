#pragma once

#include "gameobject.h"
#include <sgg/graphics.h>
#include "box.h"
#include "boundary.h"

class Player : public Box, public GameObject
{
private:
	// animated player
	std::vector<std::string> m_spritesright;
	std::vector<std::string> m_spritesleft;

	graphics::Brush m_brush_player;

	const float m_accel_horizontal = 20.0f;
	const float m_accel_vertical = 400.1f;
	const float m_max_velocity = 5.0f;
	const float m_gravity = 10.0f;
public:
	float m_vx = 0.0f;
	float m_vy = 0.0f;


public:
	void update(float dt);
	void draw();
	void init();
	Player(std::string name) : GameObject(name) {}
	float getPosX() const { return m_pos_x; }
	float getPosY() const { return m_pos_y; }


	
protected:
	void debugDraw();
	
	// dynamic motion control
	void movePlayer(float dt);
};
