<?php

namespace App\Http\Controllers;

use App\Model\Menu;
use App\Model\Restaurant;
use App\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;

class RestaurantController extends Controller
{
    public function GetAll()
    {
        $restaurants = Restaurant::all();
        return Response::json($restaurants, 200, [], JSON_NUMERIC_CHECK);
    }

    public function AddRest(Request $request)
    {
        $name = $request->name;
        $description = $request->description;
        $grade = $request->grade;
        $localization = $request->localization;
        $phone_number = $request->phone_number;
        $website = $request->website;
        $hours = $request->hours;

        if ($name != null && $description != null && $grade != null && $localization != null && $phone_number != null && $website != null && $hours != null) {
            $newRest = new Restaurant();
            $newRest->name = $name;
            $newRest->description = $description;
            $newRest->grade = $grade;
            $newRest->localization = $localization;
            $newRest->phone_number = $phone_number;
            $newRest->website = $website;
            $newRest->hours = $hours;
            $newRest->save();
            return Response::noContent(201);
        }

        return Response::noContent(400);
    }

    public function ModRest(Request $request, $id)
    {
        $name = $request->name;
        $description = $request->description;
        $grade = $request->grade;
        $localization = $request->localization;
        $phone_number = $request->phone_number;
        $website = $request->website;
        $hours = $request->hours;

        $currentRest = Restaurant::all()->where('id', $id);

        if ($currentRest->count() != 0) {
            $currentRest = $currentRest->first();
            if ($name != null && $description != null && $grade != null && $localization != null && $phone_number != null && $website != null && $hours != null) {
                $currentRest->name = $name;
                $currentRest->description = $description;
                $currentRest->grade = $grade;
                $currentRest->localization = $localization;
                $currentRest->phone_number = $phone_number;
                $currentRest->website = $website;
                $currentRest->hours = $hours;
                $currentRest->save();
                return Response::noContent(200);
            }
        }

        return Response::noContent(400);
    }

    public function DelRest($id)
    {
        $currentRest = Restaurant::all()->where('id', $id);

        if ($currentRest->count() != 0)
        {
            $currentRest = $currentRest->first();
            $menus = Menu::all()->where('restaurant_id', $id);

            foreach ($menus as $menu)
            {
                $menu->delete();
            }
            $currentRest->delete();

            return Response::noContent(200);
        }
        return Response::noContent(400);
    }
}
