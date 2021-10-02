package com.renderwaves.ld49.events;

/*
 */
public class GeneratorEvent extends GameEvent {

    public GeneratorEvent()  {
        this.eventName = "Generator Event";
        this.uniqueId = (int)(Math.random() * Integer.MAX_VALUE);
        onStart();
    }

    @Override
    public void onStart() {
        this.progress = 0;
        System.out.println(String.format("%s is Active!", this.info(), this.eventName));
    }

    @Override
    public void onEnd() {
        System.out.println(String.format("%s is Solved!", this.info(), this.eventName));
    }


    @Override
    public void onUpdate() {

    }

    @Override
    public void onRender() {

    }
}
