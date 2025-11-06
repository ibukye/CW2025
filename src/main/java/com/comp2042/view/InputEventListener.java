package com.comp2042.view;

import com.comp2042.controller.MoveEvent;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void stopGame();

    void resumeGame();

    void createNewGame();
}
