import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { NavComponent, NavService } from './nav';
import { AppRoutingModule } from './app-routing.module';
import { LoginComponent, AUTH_PROVIDERS, RegisterComponent, RegisterService } from './auth';
import { HttpService } from './shared';
import { HomeComponent } from './home/home.component';
import { UserModule } from './user';

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    ReactiveFormsModule,
    AppRoutingModule,
    UserModule
  ],
  providers: [
    AUTH_PROVIDERS,
    HttpService,
    NavService,
    RegisterService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
