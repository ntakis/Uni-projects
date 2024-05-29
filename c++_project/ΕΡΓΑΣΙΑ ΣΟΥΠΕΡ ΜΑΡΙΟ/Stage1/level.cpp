#include "level.h"
#include <sgg/graphics.h>
#include "player.h"
#include "util.h"




void Level::update(float dt)
{
	if (m_state->getPlayer()->isActive())
		m_state->getPlayer()->update(dt);

	
	
	m_boundary-> update();
	m_platform->update();
	GameObject::update(dt);
}

void Level::draw()
{
	

	
		float w = m_state->getCanvasWidth();
		float h = m_state->getCanvasHeight();

		float offset_x = m_state->m_global_offset_x / 2.0f + w / 2.0f;
		float offset_y = m_state->m_global_offset_y / 2.0f + h / 2.0f;

		// Υπολογισμός διαστάσεων για το rectangle του background
		float rectWidth =  1.5f*w;
		float rectHeight = w/1.2f;

		// Εμφάνιση πρώτου background
		graphics::drawRect(offset_x-10.0f, offset_y, rectWidth, rectHeight, m_brush_background1);
		// Εμφάνιση πιο πάνω background (ψηλότερου)
	
		graphics::drawRect(offset_x-10.0f, offset_y-8.0f, rectWidth, rectHeight, m_brush_background2);
		graphics::drawRect(offset_x-10.0f, offset_y-16.0f, rectWidth, rectHeight, m_brush_background2);
		graphics::drawRect(offset_x-10.0f, offset_y-24.0f, rectWidth, rectHeight, m_brush_background2);

		if (m_state->getPlayer()->isActive())
			m_state->getPlayer()->draw();
		m_boundary->draw();
		m_platform->draw();

	
	/*else if (m_name == "2.lvl") {
		float w = m_state->getCanvasWidth();
		float h = m_state->getCanvasHeight();

		float offset_x = m_state->m_global_offset_x / 2.0f + w / 2.0f;
		float offset_y = m_state->m_global_offset_y / 2.0f + h / 2.0f;

		float rectWidth = 1.5f * w;
		float rectHeight = w / 1.2f;

		// Εμφάνιση background LEVEL2
		graphics::drawRect(offset_x - 10.0f, offset_y, rectWidth, rectHeight, m_brush_background3);
		graphics::drawRect(offset_x - 10.0f, offset_y - 8.0f, rectWidth, rectHeight, m_brush_background3);
		graphics::drawRect(offset_x - 10.0f, offset_y - 16.0f, rectWidth, rectHeight, m_brush_background3);
		graphics::drawRect(offset_x - 10.0f, offset_y - 24.0f, rectWidth, rectHeight, m_brush_background3);

	}*/
	
}

void Level::init()
{
	// Stage 1
	for (auto p_gob : m_static_objects)
		if (p_gob) p_gob->init();
	
	for (auto p_gob : m_dynamic_objects)
		if (p_gob) p_gob->init();


	//m_terrain = new Terrain("Terrain");
	//m_terrain->init();
	
	m_boundary->init();
	m_platform->init();
}

Level::Level(const std::string & name)
	: GameObject(name)
{
	
		m_brush_background1.outline_opacity = 0.0f;
		m_brush_background1.texture = m_state->getFullAssetPath("back1.png");

		m_brush_background2.outline_opacity = 0.0f;
		m_brush_background2.texture = m_state->getFullAssetPath("back2.png");
	
	
}

Level::~Level()
{
	for (auto p_go : m_static_objects)
		delete p_go;
	for (auto p_go : m_dynamic_objects)
		delete p_go;
}
