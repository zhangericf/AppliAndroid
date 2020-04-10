<?php

/** @var \Illuminate\Database\Eloquent\Factory $factory */

use App\Model\Menu;
use App\Model\Restaurant;
use App\User;
use Faker\Generator as Faker;
use Illuminate\Support\Str;

/*
|--------------------------------------------------------------------------
| Model Factories
|--------------------------------------------------------------------------
|
| This directory should contain each of the model factory definitions for
| your application. Factories provide a convenient way to generate new
| model instances for testing / seeding your application's database.
|
*/

$factory->define(User::class, function (\Faker\Generator $faker) {
    return [
        'login' => $faker->userName,
        'name' => $faker->name,
        'firstname' => $faker->lastName,
        'password' => $faker->password,
        'email' => $faker->email,
        'age' => $faker->numberBetween(0, 99),
    ];
});

$factory->define(Restaurant::class, function (Faker $faker) {
    return [
        'name' => $faker->name,
        'description' => $faker->text,
        'grade' => $faker->randomFloat(1, 0, 10),
        'localization' => $faker->address,
        'phone_number' => $faker->phoneNumber,
        'website' => $faker->url,
        'hours' => $faker->dayOfWeek,
    ];
});

$factory->define(Menu::class, function (Faker $faker) {
    return [
        'name' => $faker->name,
        'description' => $faker->text,
        'price' => $faker->randomFloat(2,0, 20),
        'restaurant_id' => $faker->numberBetween(1, Restaurant::all()->count()),
    ];
});
