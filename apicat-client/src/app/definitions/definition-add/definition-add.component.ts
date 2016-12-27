import {Component} from "@angular/core";
import {Definition} from "../../model/definition";
import {DefinitionService} from "../definition.service";
import {Router} from "@angular/router";
import {AlertMessageService} from "../../common/alert/alert-message.service";
import {LoadingIndicatorService} from "../../common/loading-indicator/loading-indicator.service";

@Component({
  selector: 'definition-add',
  templateUrl: './definition-add.component.html',
  styleUrls: ['./definition-add.component.scss'],

})
export class DefinitionAddComponent {
  definition: Definition = new Definition();

  constructor(private definitionService: DefinitionService,
              private router: Router,
              private alertMessageService: AlertMessageService,
              private loadingService: LoadingIndicatorService) {
  }

  onSubmit(definition: Definition): void {
    this.loadingService.showSpinner();

    this.definitionService.create(definition)
      .finally(() => this.loadingService.hideSpinner())
      .subscribe(
        definition => this.router.navigate(['/definitions', definition.id]),
        error => this.alertMessageService.showErrorMessage("Unable to save definition. Error:" + error)
      );
  }
}
