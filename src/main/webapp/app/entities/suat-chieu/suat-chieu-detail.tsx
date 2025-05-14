import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './suat-chieu.reducer';

export const SuatChieuDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const suatChieuEntity = useAppSelector(state => state.suatChieu.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="suatChieuDetailsHeading">
          <Translate contentKey="projectMovieApp.suatChieu.detail.title">SuatChieu</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{suatChieuEntity.id}</dd>
          <dt>
            <span id="ngayChieu">
              <Translate contentKey="projectMovieApp.suatChieu.ngayChieu">Ngay Chieu</Translate>
            </span>
          </dt>
          <dd>
            {suatChieuEntity.ngayChieu ? <TextFormat value={suatChieuEntity.ngayChieu} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="gioChieu">
              <Translate contentKey="projectMovieApp.suatChieu.gioChieu">Gio Chieu</Translate>
            </span>
          </dt>
          <dd>{suatChieuEntity.gioChieu}</dd>
          <dt>
            <Translate contentKey="projectMovieApp.suatChieu.phong">Phong</Translate>
          </dt>
          <dd>{suatChieuEntity.phong ? suatChieuEntity.phong.id : ''}</dd>
          <dt>
            <Translate contentKey="projectMovieApp.suatChieu.phim">Phim</Translate>
          </dt>
          <dd>{suatChieuEntity.phim ? suatChieuEntity.phim.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/suat-chieu" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/suat-chieu/${suatChieuEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SuatChieuDetail;
