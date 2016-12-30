import {Component, Input, Output, EventEmitter, OnInit} from '@angular/core';


@Component({
  selector: 'app-confirmation-modal',
  templateUrl: './confirmation-modal.component.html'
})
export class ConfirmationModalComponent implements OnInit {

  @Input() title: string = 'Confirmation';
  @Input() cancelLabel: string = 'No';
  @Input() positiveLabel: string = 'Yes';

  @Output() closed: EventEmitter<ModalResult> = new EventEmitter<ModalResult>();
  @Output() loaded: EventEmitter<ConfirmationModalComponent> = new EventEmitter<ConfirmationModalComponent>();

  public visible: boolean = false;
  private topic: any;

  ngOnInit() {
    this.loaded.next(this);
  }

  show(topic: any) {
    this.visible = true;
    this.topic = topic;
  }

  public positiveAction() {
    this.visible = false;
    this.closed.next({
      topic: this.topic,
      action: ModalAction.POSITIVE
    });

    return false;
  }

  public cancelAction() {
    this.visible = false;
    this.closed.next({
      topic: this.topic,
      action: ModalAction.CANCEL
    });

    return false;
  }
}

export enum ModalAction { POSITIVE, CANCEL }

export interface ModalResult {
  action: ModalAction;
  topic: any;
}
