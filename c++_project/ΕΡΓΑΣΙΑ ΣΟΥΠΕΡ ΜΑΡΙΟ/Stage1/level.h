#pragma once

#include "gameobject.h"
#include <vector>
#include <list>
#include <string>
#include <sgg/graphics.h>
#include "player.h"
#include "boundary.h"
#include "platforms.h"

class Level : public GameObject
{


	graphics::Brush m_brush_background1;
	graphics::Brush m_brush_background2;
	

	std::list<GameObject*> m_static_objects;
	std::list<GameObject*> m_dynamic_objects;
	
	// add some collidable blocks
	std::vector<Box> m_blocks;
	std::vector<std::string> m_block_names;
	const float m_block_size = 1.0f;
	graphics::Brush m_block_brush;
	graphics::Brush m_block_brush_debug;

	float m_center_x = 5.0f;
	float m_center_y = 5.0f;

	// dedicated method to draw a block
	void drawBlock(int i);



public:
	void update(float dt) override;
	void draw() override;
	void init() override;

	Level(const std::string & name = "Level0");
	~Level() override;

private:
	
	Boundary* m_boundary = new Boundary();
	Platforms* m_platform = new Platforms();
	float m_backgroundLength=100;
	
};