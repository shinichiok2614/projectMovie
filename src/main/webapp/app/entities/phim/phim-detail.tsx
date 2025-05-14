import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './phim.reducer';

export const PhimDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const phimEntity = useAppSelector(state => state.phim.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="phimDetailsHeading">
          <Translate contentKey="projectMovieApp.phim.detail.title">Phim</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{phimEntity.id}</dd>
          <dt>
            <span id="tenPhim">
              <Translate contentKey="projectMovieApp.phim.tenPhim">Ten Phim</Translate>
            </span>
          </dt>
          <dd>{phimEntity.tenPhim}</dd>
          <dt>
            <span id="thoiLuong">
              <Translate contentKey="projectMovieApp.phim.thoiLuong">Thoi Luong</Translate>
            </span>
          </dt>
          <dd>{phimEntity.thoiLuong}</dd>
          <dt>
            <span id="gioiThieu">
              <Translate contentKey="projectMovieApp.phim.gioiThieu">Gioi Thieu</Translate>
            </span>
          </dt>
          <dd>{phimEntity.gioiThieu}</dd>
          <dt>
            <span id="ngayCongChieu">
              <Translate contentKey="projectMovieApp.phim.ngayCongChieu">Ngay Cong Chieu</Translate>
            </span>
          </dt>
          <dd>
            {phimEntity.ngayCongChieu ? <TextFormat value={phimEntity.ngayCongChieu} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="linkTrailer">
              <Translate contentKey="projectMovieApp.phim.linkTrailer">Link Trailer</Translate>
            </span>
          </dt>
          <dd>{phimEntity.linkTrailer}</dd>
          <dt>
            <span id="logo">
              <Translate contentKey="projectMovieApp.phim.logo">Logo</Translate>
            </span>
          </dt>
          <dd>
            {phimEntity.logo ? (
              <div>
                {phimEntity.logoContentType ? (
                  <a onClick={openFile(phimEntity.logoContentType, phimEntity.logo)}>
                    <img src={`data:${phimEntity.logoContentType};base64,${phimEntity.logo}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {phimEntity.logoContentType}, {byteSize(phimEntity.logo)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="doTuoi">
              <Translate contentKey="projectMovieApp.phim.doTuoi">Do Tuoi</Translate>
            </span>
          </dt>
          <dd>{phimEntity.doTuoi}</dd>
          <dt>
            <span id="theLoai">
              <Translate contentKey="projectMovieApp.phim.theLoai">The Loai</Translate>
            </span>
          </dt>
          <dd>{phimEntity.theLoai}</dd>
          <dt>
            <span id="dinhDang">
              <Translate contentKey="projectMovieApp.phim.dinhDang">Dinh Dang</Translate>
            </span>
          </dt>
          <dd>{phimEntity.dinhDang}</dd>
        </dl>
        <Button tag={Link} to="/phim" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/phim/${phimEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PhimDetail;
