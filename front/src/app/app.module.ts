import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { RouteReuseStrategy } from '@angular/router';

import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { AgendaComponent } from './component/agenda/agenda.component';
import { PacientesComponent } from './component/pacientes/pacientes.component';
import { InicioComponent } from './component/inicio/inicio.component';

import { firebaseConfig } from '../credentials/firebaseConfig';

import { RouterModule } from '@angular/router';
import { DatePickerModule } from 'ionic4-date-picker';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';


import { AngularFireModule } from '@angular/fire';
import { AngularFireAuthModule } from '@angular/fire/auth';
import { AngularFireDatabaseModule } from '@angular/fire/database';
import { AngularFirestoreModule } from '@angular/fire/firestore';
import { ChartsModule } from 'ng2-charts';


@NgModule({
  declarations: [AppComponent,
    AgendaComponent,
    PacientesComponent,
    InicioComponent],
  entryComponents: [],
  imports: [
    HttpClientModule,
    BrowserModule,
    CommonModule,
    IonicModule.forRoot(),
    AppRoutingModule,
    AngularFireModule.initializeApp(firebaseConfig),
    AngularFireAuthModule,
    AngularFireDatabaseModule,
    AngularFirestoreModule,
    RouterModule,
    DatePickerModule,
    NgxDatatableModule,
    FormsModule,
    ChartsModule
  ],
  providers: [
    StatusBar,
    AngularFirestoreModule,
    SplashScreen,
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
