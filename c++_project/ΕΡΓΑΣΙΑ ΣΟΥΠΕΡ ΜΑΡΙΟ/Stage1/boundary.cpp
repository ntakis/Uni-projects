#include "boundary.h"
#include <sgg/graphics.h>
#include "box.h"
#include "util.h"
#include "gameobject.h"
#include "player.h"
#include <iostream>

Boundary::Boundary(const std::string& name)
	: GameObject(name) {}

void Boundary::draw()
{
	for (int i = 0; i < m_blockster.size(); i++)
	{
		Box& box = m_blockster[i];
		std::string& block_names = m_blockter_names[i];
		float x = box.m_pos_x + m_state->m_global_offset_x;
		float y = box.m_pos_y + m_state->m_global_offset_y;

		m_brush_ter.texture = m_state->getFullAssetPath(block_names);

		graphics::drawRect(x, y, m_block_size, m_block_size, m_brush_ter);

		if (m_state->m_debugging)
			graphics::drawRect(x, y, m_block_size, m_block_size, m_block_brushter_debug);
	}

	for (int i = 0; i < m_blocksterside.size(); i++)
	{
		Box& box1 = m_blocksterside[i];
		std::string& block_names1 = m_blockterside_names[i];
		float x1 = box1.m_pos_x + m_state->m_global_offset_x;
		float y1 = box1.m_pos_y + m_state->m_global_offset_y;

		m_brush_terside.texture = m_state->getFullAssetPath(block_names1);

		graphics::drawRect(x1, y1, m_block_size, m_block_size, m_brush_terside);

		if (m_state->m_debugging)
			graphics::drawRect(x1, y1, m_block_size, m_block_size, m_block_brushterside_debug);
	}
}

void Boundary::init()
{
	float x_coor = -20.0f;
	float y_coor = 7.0f;
	float x_coor_left = -20.0f;
	float y_coor_left = 7.0f;
	float x_coor_right = -10.0f;
	float y_coor_right = 7.0f;

	for (int i = 0; i < 100; i++) {
		if (i < 20) {
			m_blockster.push_back(Box(x_coor, y_coor, 1.0f, 1.0f));// Προσαρμόστε τις τιμές όπως απαιτείται
			m_blockter_names.push_back("invisible-png1.png");
			x_coor += 1;
		}

		m_blocksterside.push_back(Box(x_coor_left, y_coor_left, 1.0f, 1.0f));
		// Προσαρμόστε τις τιμές όπως απαιτείται
		m_blockterside_names.push_back("invisible-png1.png");
		y_coor_left -= 1;
		m_blocksterside.push_back(Box(x_coor_right, y_coor_right, 1.0f, 1.0f));// Προσαρμόστε τις τιμές όπως απαιτείται
		m_blockterside_names.push_back("invisible-png1.png");
		y_coor_right -= 1;

	}
	m_brush_ter.outline_opacity = 0.0f;
	m_block_brushter_debug.fill_opacity = 0.1f;
	SETCOLOR(m_block_brushter_debug.fill_color, 0.1f, 1.0f, 0.1f);
	SETCOLOR(m_block_brushter_debug.outline_color, 0.3f, 1.0f, 0.2f);

	m_brush_terside.outline_opacity = 0.0f;
	m_block_brushterside_debug.fill_opacity = 0.1f;
	SETCOLOR(m_block_brushterside_debug.fill_color, 0.1f, 1.0f, 0.1f);
	SETCOLOR(m_block_brushterside_debug.outline_color, 0.3f, 1.0f, 0.2f);
}

void Boundary::update() {
	
	m_collisions->checkDownCollisions(m_blockster);
	m_collisions->checkSidewaysCollisions(m_blocksterside);
}



