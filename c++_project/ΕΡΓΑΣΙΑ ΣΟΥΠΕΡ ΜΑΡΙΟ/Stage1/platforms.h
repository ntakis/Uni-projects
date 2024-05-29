// platform.h
#pragma once
#include "gameobject.h"
#include <vector>
#include <list>
#include <string>
#include <sgg/graphics.h>
#include "player.h"
#include "collisions.h"
#include <random>

class Platforms : public GameObject {

private:
    std::vector<Box> m_rocks;
    std::vector<std::string> m_rocks_names;

    graphics::Brush m_rock_brush;
    graphics::Brush m_rock_brush_debug;
    float m_rock_direction = 1.0f;
    const float m_rock_size = 1.5f;
    float getRandomNumber(float min, float max) {
        // Δημιουργία ενός random engine
        std::random_device rd;
        std::mt19937 gen(rd());

        // Δημιουργία ενός distribution για το εύρος [min, max]
        std::uniform_int_distribution<int> dis(min, max);

        // Επιστροφή ενός τυχαίου αριθμού
        return dis(gen);
    }
    Collisions * m_collisions = new Collisions();
public:
    Platforms(const std::string& name = "Platform");
    ~Platforms() {}
    void draw();
    void init();
    void update();
    bool scpaceship_coll = false;
};
