<?php

use App\Model\Menu;
use App\Model\Restaurant;
use App\User;
use Illuminate\Support\Facades\Artisan;
use Tests\TestCase;

class UserApiTest extends TestCase {

    protected function setUp(): void
    {
        parent::setUp();
        Artisan::call('migrate');
    }

    public function testGetUsers()
    {
        for ($i = 0; $i < 3; $i++) {
            $user = factory(User::class)->create();
        }
        $response = $this->call('GET', '/api/users');

        $this->assertEquals(200, $response->getStatusCode());
        $this->assertEquals(3, User::count());
    }

    public function testGetRestaurants()
    {
        for ($i = 0; $i < 3; $i++) {
            $restaurant = factory(Restaurant::class)->create();
        }

        $response = $this->call('GET', '/api/restaurants');

        $this->assertEquals(200, $response->getStatusCode());
        $this->assertEquals(3, Restaurant::count());
        $restaurant->delete();
        $this->assertEquals(2, Restaurant::count());
    }
}
