import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './phim.reducer';

export const Phim = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const phimList = useAppSelector(state => state.phim.entities);
  const loading = useAppSelector(state => state.phim.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="phim-heading" data-cy="PhimHeading">
        <Translate contentKey="projectMovieApp.phim.home.title">Phims</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="projectMovieApp.phim.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/phim/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="projectMovieApp.phim.home.createLabel">Create new Phim</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {phimList && phimList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="projectMovieApp.phim.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('tenPhim')}>
                  <Translate contentKey="projectMovieApp.phim.tenPhim">Ten Phim</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tenPhim')} />
                </th>
                <th className="hand" onClick={sort('thoiLuong')}>
                  <Translate contentKey="projectMovieApp.phim.thoiLuong">Thoi Luong</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('thoiLuong')} />
                </th>
                <th className="hand" onClick={sort('gioiThieu')}>
                  <Translate contentKey="projectMovieApp.phim.gioiThieu">Gioi Thieu</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gioiThieu')} />
                </th>
                <th className="hand" onClick={sort('ngayCongChieu')}>
                  <Translate contentKey="projectMovieApp.phim.ngayCongChieu">Ngay Cong Chieu</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ngayCongChieu')} />
                </th>
                <th className="hand" onClick={sort('linkTrailer')}>
                  <Translate contentKey="projectMovieApp.phim.linkTrailer">Link Trailer</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('linkTrailer')} />
                </th>
                <th className="hand" onClick={sort('logo')}>
                  <Translate contentKey="projectMovieApp.phim.logo">Logo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('logo')} />
                </th>
                <th className="hand" onClick={sort('doTuoi')}>
                  <Translate contentKey="projectMovieApp.phim.doTuoi">Do Tuoi</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('doTuoi')} />
                </th>
                <th className="hand" onClick={sort('theLoai')}>
                  <Translate contentKey="projectMovieApp.phim.theLoai">The Loai</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('theLoai')} />
                </th>
                <th className="hand" onClick={sort('dinhDang')}>
                  <Translate contentKey="projectMovieApp.phim.dinhDang">Dinh Dang</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dinhDang')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {phimList.map((phim, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/phim/${phim.id}`} color="link" size="sm">
                      {phim.id}
                    </Button>
                  </td>
                  <td>{phim.tenPhim}</td>
                  <td>{phim.thoiLuong}</td>
                  <td>{phim.gioiThieu}</td>
                  <td>
                    {phim.ngayCongChieu ? <TextFormat type="date" value={phim.ngayCongChieu} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{phim.linkTrailer}</td>
                  <td>
                    {phim.logo ? (
                      <div>
                        {phim.logoContentType ? (
                          <a onClick={openFile(phim.logoContentType, phim.logo)}>
                            <img src={`data:${phim.logoContentType};base64,${phim.logo}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {phim.logoContentType}, {byteSize(phim.logo)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{phim.doTuoi}</td>
                  <td>
                    <Translate contentKey={`projectMovieApp.TheLoai.${phim.theLoai}`} />
                  </td>
                  <td>{phim.dinhDang}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/phim/${phim.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/phim/${phim.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/phim/${phim.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="projectMovieApp.phim.home.notFound">No Phims found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Phim;
