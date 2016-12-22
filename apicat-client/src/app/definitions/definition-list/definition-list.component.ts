import {Component, OnInit} from "@angular/core";
import {Definition} from "../../model/definition";
import {DefinitionService} from "../definition.service";
import {
  ModalResult,
  ConfirmationModalComponent,
  ModalAction
} from "../../common/confirmation-modal/confirmation-modal.component";

@Component({
  selector: 'definition-list',
  templateUrl: './definition-list.component.html',
  styleUrls: ['./definition-list.component.scss'],

})
export class DefinitionListComponent implements OnInit {
  definitions: Definition[];
  confirmationModal: ConfirmationModalComponent;
  errorMessage: string;

  constructor(private definitionService: DefinitionService) {
  }

  ngOnInit(): void {
    this.getDefinitions();
  }

  getDefinitions(): void {
    this.definitionService.getDefinitions()
      .subscribe(
        definitions => this.definitions = definitions,
        error => this.errorMessage = <any>error
      );
  }

  delete(definition: Definition): void {
    this.confirmationModal.show(definition);
  }

  onModalClosed(result: ModalResult): void {
    if (result.action === ModalAction.POSITIVE) {
      let definition = result.topic as Definition;

      this.definitionService
        .delete(definition.id)
        .subscribe(() => this.definitions = this.definitions.filter(d => d !== definition));
    }
  }

  onModalLoaded(modal: ConfirmationModalComponent): void {
    this.confirmationModal = modal;
  }
}
