// boundary.h
#pragma once
#include "gameobject.h"
#include <vector>
#include <list>
#include <string>
#include <sgg/graphics.h>
#include "player.h"
#include "collisions.h"

class Boundary : public GameObject {
private:
    std::vector<Box> m_blockster;
    std::vector<Box> m_blocksterside;

    std::vector<std::string> m_blockter_names;
    std::vector<std::string> m_blockterside_names;

    const float m_block_size = 1.0f;
    graphics::Brush m_brush_ter;
    graphics::Brush m_brush_terside;

    graphics::Brush m_block_brushter_debug;
    graphics::Brush m_block_brushterside_debug;
    Collisions * m_collisions = new Collisions();

public:
    Boundary(const std::string& name = "Boundary");
    void draw();
    void init();
    void update();
};
