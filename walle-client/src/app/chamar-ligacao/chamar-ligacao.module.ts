import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChamarLigacaoComponent } from './chamar-ligacao.component';
import { IonicModule } from '@ionic/angular';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from '../app-routing.module';



@NgModule({
  declarations: [ChamarLigacaoComponent],
  entryComponents: [ChamarLigacaoComponent],
  imports: [
    CommonModule,
    BrowserModule,
    IonicModule.forRoot(),
    AppRoutingModule
  ]
})
export class ChamarLigacaoModule { }
