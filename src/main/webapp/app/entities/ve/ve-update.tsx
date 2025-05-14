import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISuatChieu } from 'app/shared/model/suat-chieu.model';
import { getEntities as getSuatChieus } from 'app/entities/suat-chieu/suat-chieu.reducer';
import { IVe } from 'app/shared/model/ve.model';
import { TinhTrangVe } from 'app/shared/model/enumerations/tinh-trang-ve.model';
import { getEntity, updateEntity, createEntity, reset } from './ve.reducer';

export const VeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const suatChieus = useAppSelector(state => state.suatChieu.entities);
  const veEntity = useAppSelector(state => state.ve.entity);
  const loading = useAppSelector(state => state.ve.loading);
  const updating = useAppSelector(state => state.ve.updating);
  const updateSuccess = useAppSelector(state => state.ve.updateSuccess);
  const tinhTrangVeValues = Object.keys(TinhTrangVe);

  const handleClose = () => {
    navigate('/ve');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSuatChieus({}));
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
      ...veEntity,
      ...values,
      suatChieu: suatChieus.find(it => it.id.toString() === values.suatChieu?.toString()),
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
          tinhTrang: 'CHUA_THANH_TOAN',
          ...veEntity,
          suatChieu: veEntity?.suatChieu?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="projectMovieApp.ve.home.createOrEditLabel" data-cy="VeCreateUpdateHeading">
            <Translate contentKey="projectMovieApp.ve.home.createOrEditLabel">Create or edit a Ve</Translate>
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
                <ValidatedField name="id" required readOnly id="ve-id" label={translate('global.field.id')} validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label={translate('projectMovieApp.ve.soDienThoai')}
                id="ve-soDienThoai"
                name="soDienThoai"
                data-cy="soDienThoai"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('projectMovieApp.ve.email')} id="ve-email" name="email" data-cy="email" type="text" />
              <ValidatedField
                label={translate('projectMovieApp.ve.giaTien')}
                id="ve-giaTien"
                name="giaTien"
                data-cy="giaTien"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('projectMovieApp.ve.tinhTrang')}
                id="ve-tinhTrang"
                name="tinhTrang"
                data-cy="tinhTrang"
                type="select"
              >
                {tinhTrangVeValues.map(tinhTrangVe => (
                  <option value={tinhTrangVe} key={tinhTrangVe}>
                    {translate('projectMovieApp.TinhTrangVe.' + tinhTrangVe)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="ve-suatChieu"
                name="suatChieu"
                data-cy="suatChieu"
                label={translate('projectMovieApp.ve.suatChieu')}
                type="select"
              >
                <option value="" key="0" />
                {suatChieus
                  ? suatChieus.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ve" replace color="info">
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

export default VeUpdate;
