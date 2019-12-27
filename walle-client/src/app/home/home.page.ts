import { Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { ModalController } from '@ionic/angular';
import { ChamarLigacaoComponent } from '../chamar-ligacao/chamar-ligacao.component';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {
  url = 'https://graacc.org.br';
  urlWhats = 'https://wa.me/558393784812?text=Ol%C3%A1%20seja%20bem%20vindo';

  constructor(public sanitizer: DomSanitizer,  private modalCtrl: ModalController) {}

  async moveToFirst() {
    const modal = await this.modalCtrl.create({
     component: ChamarLigacaoComponent
   });

    return await modal.present();
  }

}
