import { Component, OnInit } from '@angular/core';
import { ModalController } from '@ionic/angular';

@Component({
  selector: 'app-chamar-ligacao',
  templateUrl: './chamar-ligacao.component.html',
  styleUrls: ['./chamar-ligacao.component.scss'],
})
export class ChamarLigacaoComponent implements OnInit {

  constructor(private modalCtrl: ModalController) { }

  ngOnInit() {}

  closeModal() {
    this.modalCtrl.dismiss();
  }

}
