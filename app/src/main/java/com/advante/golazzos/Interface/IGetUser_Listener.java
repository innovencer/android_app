package com.advante.golazzos.Interface;

import com.advante.golazzos.Model.Liga;
import com.advante.golazzos.Model.User;

import java.util.ArrayList;

/**
 * Created by Ruben Flores on 6/28/2016.
 */
public interface IGetUser_Listener {
    void onComplete(Boolean complete, User user);
}
