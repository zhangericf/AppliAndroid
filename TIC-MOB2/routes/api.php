<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::group(['middleware' => ['web']], function() {
    Route::get('users', 'UserController@GetAll');
    Route::post('register', 'UserController@Register');
    Route::post('auth', 'UserController@Login');

    Route::get('restaurants', 'RestaurantController@GetAll');
    Route::post('restaurant', 'RestaurantController@AddRest');

    Route::prefix('restaurant')->group(function () {
        Route::put('/{id}', 'RestaurantController@ModRest');
        Route::delete('/{id}', 'RestaurantController@DelRest');

        Route::prefix('/{id}')->group(function () {
            Route::get('/menus', 'MenuController@GetAll');
            Route::post('/menu', 'MenuController@AddMenu');
            Route::put('/menu/{menu_id}', 'MenuController@ModMenu');
            Route::delete('/menu/{menu_id}', 'MenuController@DelMenu');

            Route::get('/avis', 'AvisController@GetAll');
            Route::post('/avis', 'AvisController@AddAvis');
            Route::put('/avis/{avis_id}', 'AvisController@ModAvis');
            Route::delete('/avis/{avis_id}', 'AvisController@DelAvis');
        });
    });
});
