<?php

namespace App\Http\Controllers;

use App\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use mysql_xdevapi\Exception;

class UserController extends Controller
{
    public function GetAll()
    {
        $users = User::all();
        return Response::json($users, 200, [], JSON_NUMERIC_CHECK);
    }

    public function Register(Request $request)
    {
        $login = $request->login;
        $password = $request->password;
        $email = $request->email;
        $name = $request->name;
        $firstname = $request->firstname;
        $age = $request->age;

        if (User::all()->where('login', $login)->count() != 0)
        {
            return Response::noContent(400);
        }

        if ($login != null && $password != null && $email != null && $name != null && $firstname != null && $age != null) {
            $newUser = new User();
            $newUser->login = $login;
            $newUser->password = $password;
            $newUser->email = $email;
            $newUser->name = $name;
            $newUser->firstname = $firstname;
            $newUser->age = $age;
            $newUser->save();
            return Response::noContent(201);
        }

        return Response::noContent(400);
    }

    public function Login(Request $request)
    {
        $login = $request->login;
        $password = $request->password;

        if ($login == null || $password == null)
        {
            return Response::noContent(400);
        }

        $user = User::all()->where('login', $login)->first();
        
        if ($user != null)
            if ($user->password == $password)
                return Response::noContent(200);
        return Response::noContent(400);
    }
}
