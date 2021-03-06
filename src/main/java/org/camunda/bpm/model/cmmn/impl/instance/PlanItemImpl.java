/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.model.cmmn.impl.instance;

import static org.camunda.bpm.model.cmmn.impl.CmmnModelConstants.CMMN10_NS;
import static org.camunda.bpm.model.cmmn.impl.CmmnModelConstants.CMMN_ATTRIBUTE_DEFINITION_REF;
import static org.camunda.bpm.model.cmmn.impl.CmmnModelConstants.CMMN_ATTRIBUTE_ENTRY_CRITERIA_REFS;
import static org.camunda.bpm.model.cmmn.impl.CmmnModelConstants.CMMN_ATTRIBUTE_EXIT_CRITERIA_REFS;
import static org.camunda.bpm.model.cmmn.impl.CmmnModelConstants.CMMN_ATTRIBUTE_NAME;
import static org.camunda.bpm.model.cmmn.impl.CmmnModelConstants.CMMN_ELEMENT_PLAN_ITEM;

import java.util.Collection;

import org.camunda.bpm.model.cmmn.instance.CmmnElement;
import org.camunda.bpm.model.cmmn.instance.PlanItem;
import org.camunda.bpm.model.cmmn.instance.PlanItemDefinition;
import org.camunda.bpm.model.cmmn.instance.Sentry;
import org.camunda.bpm.model.xml.ModelBuilder;
import org.camunda.bpm.model.xml.impl.instance.ModelTypeInstanceContext;
import org.camunda.bpm.model.xml.type.ModelElementTypeBuilder;
import org.camunda.bpm.model.xml.type.ModelElementTypeBuilder.ModelTypeInstanceProvider;
import org.camunda.bpm.model.xml.type.attribute.Attribute;
import org.camunda.bpm.model.xml.type.child.ChildElement;
import org.camunda.bpm.model.xml.type.child.SequenceBuilder;
import org.camunda.bpm.model.xml.type.reference.AttributeReference;
import org.camunda.bpm.model.xml.type.reference.AttributeReferenceCollection;

/**
 * @author Roman Smirnov
 *
 */
public class PlanItemImpl extends CmmnElementImpl implements PlanItem {

  protected static Attribute<String> nameAttribute;
  protected static AttributeReference<PlanItemDefinition> planItemDefinitionRefAttribute;
  protected static AttributeReferenceCollection<Sentry> entryCriteriaRefCollection;
  protected static AttributeReferenceCollection<Sentry> exitCriteriaRefCollection;
  protected static ChildElement<ItemControl> itemControlChild;

  public PlanItemImpl(ModelTypeInstanceContext instanceContext) {
    super(instanceContext);
  }

  public String getName() {
    return nameAttribute.getValue(this);
  }

  public void setName(String name) {
    nameAttribute.setValue(this, name);
  }

  public PlanItemDefinition getDefinition() {
    return planItemDefinitionRefAttribute.getReferenceTargetElement(this);
  }

  public void setDefinition(PlanItemDefinition definition) {
    planItemDefinitionRefAttribute.setReferenceTargetElement(this, definition);
  }

  public Collection<Sentry> getEntryCriterias() {
    return entryCriteriaRefCollection.getReferenceTargetElements(this);
  }

  public Collection<Sentry> getExitCriterias() {
    return exitCriteriaRefCollection.getReferenceTargetElements(this);
  }

  public ItemControl getItemControl() {
    return itemControlChild.getChild(this);
  }

  public void setItemControl(ItemControl itemControl) {
    itemControlChild.setChild(this, itemControl);
  }

  public static void registerType(ModelBuilder modelBuilder) {
    ModelElementTypeBuilder typeBuilder = modelBuilder.defineType(PlanItem.class, CMMN_ELEMENT_PLAN_ITEM)
        .namespaceUri(CMMN10_NS)
        .extendsType(CmmnElement.class)
        .instanceProvider(new ModelTypeInstanceProvider<PlanItem>() {
          public PlanItem newInstance(ModelTypeInstanceContext instanceContext) {
            return new PlanItemImpl(instanceContext);
          }
        });

    nameAttribute = typeBuilder.stringAttribute(CMMN_ATTRIBUTE_NAME)
        .build();

    planItemDefinitionRefAttribute = typeBuilder.stringAttribute(CMMN_ATTRIBUTE_DEFINITION_REF)
        .idAttributeReference(PlanItemDefinition.class)
        .build();

    entryCriteriaRefCollection = typeBuilder.stringAttribute(CMMN_ATTRIBUTE_ENTRY_CRITERIA_REFS)
        .idAttributeReferenceCollection(Sentry.class, CmmnAttributeElementReferenceCollection.class)
        .build();

    exitCriteriaRefCollection = typeBuilder.stringAttribute(CMMN_ATTRIBUTE_EXIT_CRITERIA_REFS)
        .idAttributeReferenceCollection(Sentry.class, CmmnAttributeElementReferenceCollection.class)
        .build();

    SequenceBuilder sequenceBuilder = typeBuilder.sequence();

    itemControlChild = sequenceBuilder.element(ItemControl.class)
        .build();

    typeBuilder.build();
  }

}
