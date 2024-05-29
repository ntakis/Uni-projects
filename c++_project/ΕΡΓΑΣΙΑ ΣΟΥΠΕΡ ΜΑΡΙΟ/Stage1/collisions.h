// collisions.h
#pragma once
#include <vector>
#include "box.h"  // ���������� ��� ����� ��� ����� Box
#include "gamestate.h"  // ���������� ��� ����� ��� ����� GameState
#include "gameobject.h"  // ���������� ��� ����� ��� ����� GameState

class Collisions: public GameObject
{
public:
    Collisions(const std::string& name = "Colission");  // ������������� ��� �������� ���� ������ ���� GameState
    void checkDownCollisions(const std::vector<Box>& objects);
    void checkSidewaysCollisions(const std::vector<Box>& objects);
    bool checkSpaceshipCollision(const Box& spaceship);
  // ������� ���� ��� ����� GameState
};


