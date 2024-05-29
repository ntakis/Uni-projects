// collisions.cpp

#include "collisions.h"
#include "gamestate.h"
#include "player.h"
#include "gameobject.h"
#include <iostream>

Collisions::Collisions(const std::string& name)
    : GameObject(name) {}

void Collisions::checkDownCollisions(const std::vector<Box>& objects)
{
    for (const auto& block : objects)  // Χρησιμοποιούμε const auto&
    {
        ;
        float offset = 0.0f;

        if ((offset = const_cast<Player*>(m_state->getPlayer())->intersectDown(const_cast<Box&>(block))) != 0.0f)
        {
            m_state->getPlayer()->m_pos_y += offset;

            if (m_state->getPlayer()->m_vy > 1.0f)
                m_state->getPlayer()->m_vy = 0.0f;

            break;
        }
    }
}

void Collisions::checkSidewaysCollisions(const std::vector<Box>& objects)
{
    if (!objects.empty()) {
        for (auto& block : objects)
        {
            float offset = 0.0f;
            if ((offset = const_cast<Player*>(m_state->getPlayer())->intersectSideways(const_cast<Box&>(block))) != 0.0f)
            {
                m_state->getPlayer()->m_pos_x += offset;
                m_state->getPlayer()->m_vx = 0.0f;
                break;
            }
        }
    }
}

bool Collisions::checkSpaceshipCollision(const Box& spaceship)
{
    float offset = 0.0f;
    if ((offset = const_cast<Player*>(m_state->getPlayer())->intersect(const_cast<Box&>(spaceship)) != 0.0f))
    {
        std::cout << "Player collided with the spaceship!" << std::endl;
        return true;
    }
    return false;
}
