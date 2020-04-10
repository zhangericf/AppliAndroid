<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateAvis extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('avis', function (Blueprint $table) {
            $table->increments('id');
            $table->string('content');
            $table->float('grade');
            $table->integer('restaurant_id')->unsigned();
            $table->integer('user_id');
            $table->timestamps();
        });

        Schema::table('avis', function (Blueprint $table)
        {
            $table->foreign('restaurant_id')->references('id')->on('restaurant')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('avis');
    }
}
