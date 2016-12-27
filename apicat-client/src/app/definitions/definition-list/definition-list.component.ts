import {Component, OnInit} from "@angular/core";
import {Definition} from "../../model/definition";
import {DefinitionService} from "../definition.service";
import {
  ModalResult,
  ConfirmationModalComponent,
  ModalAction
} from "../../common/confirmation-modal/confirmation-modal.component";
import {AlertMessageService} from "../../common/alert/alert-message.service";
import {LoadingIndicatorService} from "../../common/loading-indicator/loading-indicator.service";

@Component({
  selector: 'definition-list',
  templateUrl: './definition-list.component.html',
  styleUrls: ['./definition-list.component.scss'],

})
export class DefinitionListComponent implements OnInit {
  definitions: Definition[];
  confirmationModal: ConfirmationModalComponent;

  constructor(private definitionService: DefinitionService,
              private alertMessageService: AlertMessageService,
              private loadingService: LoadingIndicatorService) {
  }

  ngOnInit(): void {
    this.getDefinitions();
  }

  getDefinitions(): void {
    this.loadingService.showSpinner();
    this.definitionService.getDefinitions()
      .finally(() => this.loadingService.hideSpinner())
      .subscribe(
        definitions => this.definitions = definitions,
        error => this.alertMessageService.showErrorMessage("Unable to fetch definitions. Error:" + error));
  }

  delete(definition: Definition): void {
    this.confirmationModal.show(definition);
  }

  onModalClosed(result: ModalResult): void {
    if (result.action === ModalAction.POSITIVE) {
      let definition = result.topic as Definition;
      this.deleteDefinition(definition);
    }
  }

  private deleteDefinition(definition: Definition): void {
    this.loadingService.showSpinner();

    this.definitionService
      .delete(definition.id)
      .finally(() => this.loadingService.hideSpinner())
      .subscribe(
        () => this.definitions = this.definitions.filter(d => d !== definition),
        error => this.alertMessageService.showErrorMessage("Unable to delete definition. Error:" + error)
      );
  }

  onModalLoaded(modal: ConfirmationModalComponent): void {
    this.confirmationModal = modal;
  }

}
