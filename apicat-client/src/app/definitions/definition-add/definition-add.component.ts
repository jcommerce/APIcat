import {Component} from "@angular/core";
import {Definition} from "../../model/definition";
import {DefinitionService} from "../definition.service";
import {Router} from "@angular/router";
import {AlertMessageService} from "../../common/alert/alert-message.service";

@Component({
  selector: 'definition-add',
  templateUrl: './definition-add.component.html',
  styleUrls: ['./definition-add.component.scss'],

})
export class DefinitionAddComponent {
  definition: Definition = new Definition();

  constructor(private definitionService: DefinitionService,
              private router: Router,
              private alertMessageService: AlertMessageService) {
  }

  onSubmit(definition: Definition): void {
    this.definitionService.create(definition)
      .subscribe(
        definition => this.router.navigate(['/definitions', definition.id]),
        error => this.alertMessageService.showErrorMessage("Unable to save definition. Error:" + error)
      );
  }
}
