<?php

namespace App\Http\Controllers;

use App\Model\Avis;
use App\Model\Restaurant;
use App\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;

class AvisController extends Controller
{
    public function GetAll($id)
    {
        $Avis = Avis::where('restaurant_id', $id)->get();
        return Response::json($Avis, 200, [], JSON_NUMERIC_CHECK);
    }

    public function AddAvis(Request $request, $id)
    {
        $content = $request->content;
        $user_id = $request->user_id;
        $grade = $request->grade;
        $userExist = User::all()->where('id', $user_id)->first();
        $restaurantExist = Restaurant::all()->where('id', $id)->first();

        if ($restaurantExist != null && $userExist != null && $content != null)
        {
            $newAvis = new Avis();
            $newAvis->content = $content;
            $newAvis->grade = $grade;
            $newAvis->restaurant_id = $id;
            $newAvis->user_id = $user_id;
            $newAvis->save();
            return Response::noContent(201);
        }

        return Response::noContent(400);
    }

    public function ModAvis(Request $request, $id, $avis_id)
    {
        $content = $request->content;
        $grade = $request->grade;

        $currentAvis = Avis::all()->where('restaurant_id', $id);
        $currentAvis = $currentAvis->where('id', $avis_id);

        if ($currentAvis->count() != 0)
        {
            $currentAvis = $currentAvis->first();
            if ($content != null && $grade != null)
            {
                $currentAvis->content = $content;
                $currentAvis->grade = $grade;
                $currentAvis->save();
                return Response::noContent(200);
            }
        }
        return Response::noContent(400);
    }

    public function DelAvis($id, $Avis_id)
    {
        $currentAvis = Avis::all()->where('restaurant_id', $id);
        $currentAvis = $currentAvis->where('id', $Avis_id);

        if ($currentAvis->count() != 0)
        {
            $currentAvis = $currentAvis->first();
            $currentAvis->delete();
            return Response::noContent(200);
        }
        return Response::noContent(400);
    }
}
