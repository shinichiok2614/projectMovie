import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICumRap } from 'app/shared/model/cum-rap.model';
import { getEntities as getCumRaps } from 'app/entities/cum-rap/cum-rap.reducer';
import { IRap } from 'app/shared/model/rap.model';
import { getEntity, updateEntity, createEntity, reset } from './rap.reducer';

export const RapUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cumRaps = useAppSelector(state => state.cumRap.entities);
  const rapEntity = useAppSelector(state => state.rap.entity);
  const loading = useAppSelector(state => state.rap.loading);
  const updating = useAppSelector(state => state.rap.updating);
  const updateSuccess = useAppSelector(state => state.rap.updateSuccess);

  const handleClose = () => {
    navigate('/rap');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCumRaps({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...rapEntity,
      ...values,
      cumRap: cumRaps.find(it => it.id.toString() === values.cumRap?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...rapEntity,
          cumRap: rapEntity?.cumRap?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="projectMovieApp.rap.home.createOrEditLabel" data-cy="RapCreateUpdateHeading">
            <Translate contentKey="projectMovieApp.rap.home.createOrEditLabel">Create or edit a Rap</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="rap-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('projectMovieApp.rap.tenRap')}
                id="rap-tenRap"
                name="tenRap"
                data-cy="tenRap"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('projectMovieApp.rap.diaChi')} id="rap-diaChi" name="diaChi" data-cy="diaChi" type="text" />
              <ValidatedField
                label={translate('projectMovieApp.rap.thanhPho')}
                id="rap-thanhPho"
                name="thanhPho"
                data-cy="thanhPho"
                type="text"
              />
              <ValidatedField id="rap-cumRap" name="cumRap" data-cy="cumRap" label={translate('projectMovieApp.rap.cumRap')} type="select">
                <option value="" key="0" />
                {cumRaps
                  ? cumRaps.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/rap" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default RapUpdate;
