#pragma once
#include <chrono>
#include <thread>
#include <stdlib.h>
#include <cmath>
#include <chrono>¡
#include <iostream>
#include <list>
using namespace std;

#define SETCOLOR(c, r, g, b) {c[0]=r; c[1]=g; c[2]=b;}
#define AP ".\\assets\\"

//make sleep function¡
inline void sleep(int ms)
{
    this_thread::sleep_for(chrono::milliseconds(ms));
}
