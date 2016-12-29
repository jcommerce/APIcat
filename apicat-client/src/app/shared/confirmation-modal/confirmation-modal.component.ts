import {Component, Input, Output, EventEmitter, OnInit} from "@angular/core";


@Component({
  selector: 'confirmation-modal',
  templateUrl: './confirmation-modal.component.html'
})
export class ConfirmationModalComponent implements OnInit {

  @Input('title') private title: string = 'Confirmation';
  @Input('cancel-label') private cancelLabel: string = 'No';
  @Input('positive-label') private positiveLabel: string = 'Yes';

  /**
   * Fires an event when the modal is closed. The argument indicated how it was closed.
   * @type {EventEmitter<ModalResult>}
   */
  @Output('closed') private closeEmitter: EventEmitter<ModalResult> = new EventEmitter<ModalResult>();
  /**
   * Fires an event when the modal is ready with a pointer to the modal.
   * @type {EventEmitter<ConfirmationModalComponent>}
   */
  @Output('loaded') private loadedEmitter: EventEmitter<ConfirmationModalComponent> = new EventEmitter<ConfirmationModalComponent>();

  private visible: boolean = false;
  private topic: any = null;

  ngOnInit() {
    this.loadedEmitter.next(this);
  }

  show(topic: any) {
    this.visible = true;
    this.topic = topic;
  }

  private positiveAction() {
    this.visible = false;
    this.closeEmitter.next({
      topic: this.topic,
      action: ModalAction.POSITIVE
    });

    return false;
  }

  private cancelAction() {
    this.visible = false;
    this.closeEmitter.next({
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
