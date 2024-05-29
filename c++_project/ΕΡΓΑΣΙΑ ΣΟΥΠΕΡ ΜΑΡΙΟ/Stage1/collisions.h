// collisions.h
#pragma once
#include <vector>
#include "box.h"  // Υποθέτουμε ότι έχετε μια κλάση Box
#include "gamestate.h"  // Υποθέτουμε ότι έχετε μια κλάση GameState
#include "gameobject.h"  // Υποθέτουμε ότι έχετε μια κλάση GameState

class Collisions: public GameObject
{
public:
    Collisions(const std::string& name = "Colission");  // Κατασκευαστής που λαμβάνει έναν δείκτη προς GameState
    void checkDownCollisions(const std::vector<Box>& objects);
    void checkSidewaysCollisions(const std::vector<Box>& objects);
    bool checkSpaceshipCollision(const Box& spaceship);
  // Δείκτης προς την κλάση GameState
};


