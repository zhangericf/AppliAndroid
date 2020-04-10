<?php

namespace App\Http\Controllers;

use App\Model\Menu;
use App\Model\Restaurant;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;

class MenuController extends Controller
{
    public function GetAll($id)
    {
        $menus = Menu::where('restaurant_id', $id)->get();
        return Response::json($menus, 200, [], JSON_NUMERIC_CHECK);
    }

    public function AddMenu(Request $request, $id)
    {
        $name = $request->name;
        $description = $request->description;
        $price = $request->price;
        $restaurantExist = Restaurant::all()->where('id', $id)->first();

        if ($restaurantExist != null && $name != null && $description != null && $price != null) {
            $newMenu = new Menu();
            $newMenu->name = $name;
            $newMenu->description = $description;
            $newMenu->price = $price;
            $newMenu->restaurant_id = $id;
            $newMenu->save();
            return Response::noContent(201);
        }

        return Response::noContent(400);
    }

    public function ModMenu(Request $request, $id, $menu_id)
    {
        $name = $request->name;
        $description = $request->description;
        $price = $request->price;

        $currentMenu = Menu::all()->where('restaurant_id', $id);
        $currentMenu = $currentMenu->where('id', $menu_id);

        if ($currentMenu->count() != 0)
        {
            $currentMenu = $currentMenu->first();
            if ($name != null && $description != null && $price != null) {
                $currentMenu->name = $name;
                $currentMenu->description = $description;
                $currentMenu->price = $price;
                $currentMenu->save();
                return Response::noContent(200);
            }
        }
        return Response::noContent(400);
    }

    public function DelMenu($id, $menu_id)
    {
        $currentMenu = Menu::all()->where('restaurant_id', $id);
        $currentMenu = $currentMenu->where('id', $menu_id);

        if ($currentMenu->count() != 0)
        {
            $currentMenu = $currentMenu->first();
            $currentMenu->delete();
            return Response::noContent(200);
        }
        return Response::noContent(400);
    }
}
