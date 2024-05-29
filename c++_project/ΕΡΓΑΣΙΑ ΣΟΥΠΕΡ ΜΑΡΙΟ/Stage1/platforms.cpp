#include "platforms.h"
#include <sgg/graphics.h>
#include "box.h"
#include "util.h"
#include "gameobject.h"
#include "player.h"
#include <iostream>
Platforms::Platforms(const std::string& name) : GameObject(name)
{
    // Εδώ μπορείτε να προσθέσετε οποιοδήποτε επιπλέον κώδικα χρειάζεται ο κατασκευαστής.
}

void Platforms::draw()
{
	for (int i = 0; i < m_rocks.size(); i++)
	{
		Box& box1 = m_rocks[i];
		std::string& rock_names1 = m_rocks_names[i];
		float x1 = box1.m_pos_x + m_state->m_global_offset_x;
		float y1 = box1.m_pos_y + m_state->m_global_offset_y;

		m_rock_brush.texture = m_state->getFullAssetPath(rock_names1);

		graphics::drawRect(x1, y1, m_rock_size, m_rock_size, m_rock_brush);

		if (m_state->m_debugging)
			graphics::drawRect(x1, y1, 0.8f, 0.6f, m_rock_brush_debug);
	}
}



void Platforms::init()
{
	float x_coor_rock_cur = -17.0f;
	float y_coor_rock = 3.0f;
	float x_coor_rock_pre;
	for (int i = 0; i < 10; i++) {
		m_rocks.push_back(Box(x_coor_rock_cur, y_coor_rock, 0.75f, 0.5f));// Προσαρμόστε τις τιμές όπως απαιτείται
		m_rocks_names.push_back("rock.png");
		y_coor_rock -= 3.5f;
		x_coor_rock_pre = x_coor_rock_cur;
		x_coor_rock_cur = getRandomNumber(-19.0f, -11.0f);
		while (abs(x_coor_rock_cur - x_coor_rock_pre) > 6.0f) {
			x_coor_rock_cur = getRandomNumber(-19.0f, -11.0f);
		}
	}

	x_coor_rock_cur = -15.0f;
	y_coor_rock = -32.0f;
	m_rocks.push_back(Box(x_coor_rock_cur, y_coor_rock, 0.75f, 0.5f));// Προσαρμόστε τις τιμές όπως απαιτείται
	m_rocks_names.push_back("spaceship.png");
	m_rock_brush.outline_opacity = 0.0f;
	m_rock_brush_debug.fill_opacity = 0.1f;
	SETCOLOR(m_rock_brush_debug.fill_color, 0.1f, 1.0f, 0.1f);
	SETCOLOR(m_rock_brush_debug.outline_color, 0.3f, 1.0f, 0.2f);
}

void Platforms::update()
{
	m_collisions->checkDownCollisions(m_rocks);
	m_collisions->checkSidewaysCollisions(m_rocks);
	if (m_collisions->checkSpaceshipCollision(m_rocks.back())) {
		scpaceship_coll = true;
		
	}
	std::cout << scpaceship_coll << std::endl;
	

}



