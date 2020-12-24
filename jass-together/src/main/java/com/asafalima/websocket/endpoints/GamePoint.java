package com.asafalima.websocket.endpoints;

import com.asafalima.websocket.services.GameRouter;

import java.util.List;


@RestController
public class GamePoint {


    @RequestMapping("/")
    public List<String> list()
    {
        return gr.rooms();
    }
}
