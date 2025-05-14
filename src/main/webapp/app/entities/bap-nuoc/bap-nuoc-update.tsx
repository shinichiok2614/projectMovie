import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBapNuoc } from 'app/shared/model/bap-nuoc.model';
import { getEntity, updateEntity, createEntity, reset } from './bap-nuoc.reducer';

export const BapNuocUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bapNuocEntity = useAppSelector(state => state.bapNuoc.entity);
  const loading = useAppSelector(state => state.bapNuoc.loading);
  const updating = useAppSelector(state => state.bapNuoc.updating);
  const updateSuccess = useAppSelector(state => state.bapNuoc.updateSuccess);

  const handleClose = () => {
    navigate('/bap-nuoc');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.giaTien !== undefined && typeof values.giaTien !== 'number') {
      values.giaTien = Number(values.giaTien);
    }

    const entity = {
      ...bapNuocEntity,
      ...values,
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
          ...bapNuocEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="projectMovieApp.bapNuoc.home.createOrEditLabel" data-cy="BapNuocCreateUpdateHeading">
            <Translate contentKey="projectMovieApp.bapNuoc.home.createOrEditLabel">Create or edit a BapNuoc</Translate>
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
                  id="bap-nuoc-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('projectMovieApp.bapNuoc.tenBapNuoc')}
                id="bap-nuoc-tenBapNuoc"
                name="tenBapNuoc"
                data-cy="tenBapNuoc"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedBlobField
                label={translate('projectMovieApp.bapNuoc.logo')}
                id="bap-nuoc-logo"
                name="logo"
                data-cy="logo"
                isImage
                accept="image/*"
              />
              <ValidatedField
                label={translate('projectMovieApp.bapNuoc.giaTien')}
                id="bap-nuoc-giaTien"
                name="giaTien"
                data-cy="giaTien"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bap-nuoc" replace color="info">
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

export default BapNuocUpdate;
