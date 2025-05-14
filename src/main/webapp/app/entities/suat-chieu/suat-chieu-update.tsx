import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPhong } from 'app/shared/model/phong.model';
import { getEntities as getPhongs } from 'app/entities/phong/phong.reducer';
import { IPhim } from 'app/shared/model/phim.model';
import { getEntities as getPhims } from 'app/entities/phim/phim.reducer';
import { ISuatChieu } from 'app/shared/model/suat-chieu.model';
import { getEntity, updateEntity, createEntity, reset } from './suat-chieu.reducer';

export const SuatChieuUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const phongs = useAppSelector(state => state.phong.entities);
  const phims = useAppSelector(state => state.phim.entities);
  const suatChieuEntity = useAppSelector(state => state.suatChieu.entity);
  const loading = useAppSelector(state => state.suatChieu.loading);
  const updating = useAppSelector(state => state.suatChieu.updating);
  const updateSuccess = useAppSelector(state => state.suatChieu.updateSuccess);

  const handleClose = () => {
    navigate('/suat-chieu');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPhongs({}));
    dispatch(getPhims({}));
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
      ...suatChieuEntity,
      ...values,
      phong: phongs.find(it => it.id.toString() === values.phong?.toString()),
      phim: phims.find(it => it.id.toString() === values.phim?.toString()),
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
          ...suatChieuEntity,
          phong: suatChieuEntity?.phong?.id,
          phim: suatChieuEntity?.phim?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="projectMovieApp.suatChieu.home.createOrEditLabel" data-cy="SuatChieuCreateUpdateHeading">
            <Translate contentKey="projectMovieApp.suatChieu.home.createOrEditLabel">Create or edit a SuatChieu</Translate>
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
                  id="suat-chieu-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('projectMovieApp.suatChieu.ngayChieu')}
                id="suat-chieu-ngayChieu"
                name="ngayChieu"
                data-cy="ngayChieu"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('projectMovieApp.suatChieu.gioChieu')}
                id="suat-chieu-gioChieu"
                name="gioChieu"
                data-cy="gioChieu"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="suat-chieu-phong"
                name="phong"
                data-cy="phong"
                label={translate('projectMovieApp.suatChieu.phong')}
                type="select"
              >
                <option value="" key="0" />
                {phongs
                  ? phongs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="suat-chieu-phim"
                name="phim"
                data-cy="phim"
                label={translate('projectMovieApp.suatChieu.phim')}
                type="select"
              >
                <option value="" key="0" />
                {phims
                  ? phims.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/suat-chieu" replace color="info">
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

export default SuatChieuUpdate;
